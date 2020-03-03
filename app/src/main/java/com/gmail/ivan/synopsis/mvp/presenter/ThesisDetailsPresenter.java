package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisDetailsContract;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThesisDetailsPresenter
        extends BasePresenter<ThesisDetailsContract.View, ThesisDetailsContract.Router>
        implements ThesisDetailsContract.Presenter {

    private static final String TAG = ThesisDetailsPresenter.class.getSimpleName();

    @NonNull
    private final AppDataBase dataBase;

    @Nullable
    private Thesis thesis;

    public ThesisDetailsPresenter(@NonNull ThesisDetailsContract.Router router,
                                  @NonNull AppDataBase dataBase) {
        super(router);
        this.dataBase = dataBase;
    }

    @Override
    public void loadThesis(int thesisId) {
        try {
            thesis = new LoadThesisTask(dataBase).execute(thesisId)
                                                 .get();
            Objects.requireNonNull(getView())
                   .loadThesis(Objects.requireNonNull(thesis));
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "loadThesis: ", e);
        }
    }

    @Override
    public void showEdit() {
        Objects.requireNonNull(getView())
               .showEditThesis();
    }

    @Override
    public void saveChanges() {
        new UpdateThesisTask(dataBase).execute(thesis);
        Objects.requireNonNull(getView())
               .showThesisDetails();
    }

    @Override
    public void showConfirmChanges() {
        getRouter().showConfirmChanges();
    }

    private static class UpdateThesisTask extends AsyncTask<Thesis, Void, Void> {

        @NonNull
        private final AppDataBase dataBase;

        UpdateThesisTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Void doInBackground(Thesis... theses) {
            dataBase.thesisRepository()
                    .updateThesis(theses[0]);
            return null;
        }
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
