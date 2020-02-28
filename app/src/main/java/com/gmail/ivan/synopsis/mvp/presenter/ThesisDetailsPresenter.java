package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisDetailsContract;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;

public class ThesisDetailsPresenter
        extends BasePresenter<ThesisDetailsContract.View, ThesisDetailsContract.Router>
        implements ThesisDetailsContract.Presenter {

    private static final String TAG = ThesisDetailsPresenter.class.getSimpleName();

    @NonNull
    private final AppDataBase dataBase;

    public ThesisDetailsPresenter(@NonNull ThesisDetailsContract.Router router,
                                  @NonNull AppDataBase dataBase) {
        super(router);
        this.dataBase = dataBase;
    }

    @Override
    public void loadThesis(int thesisId) {
        try {
            Thesis thesis = new LoadThesisTask(dataBase).execute(thesisId)
                                                        .get();
            Objects.requireNonNull(getView())
                   .showThesis(thesis);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "loadThesis: ", e);
        }
    }

    @Override
    public void showEditThesis(@NonNull Thesis thesis) {
        getRouter().showEditThesis(thesis);
    }

    private static class LoadThesisTask extends AsyncTask<Integer, Void, Thesis> {

        @NonNull
        private final AppDataBase dataBase;

        LoadThesisTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Thesis doInBackground(Integer... integers) {
            return dataBase.thesisRepository()
                           .getThesis(integers[0]);
        }
    }
}
