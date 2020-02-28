package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisEditContract;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;

public class ThesisEditPresenter
        extends BasePresenter<ThesisEditContract.View, ThesisEditContract.Router>
        implements ThesisEditContract.Presenter {

    private static final String TAG = ThesisEditPresenter.class.getSimpleName();

    @NonNull
    private final AppDataBase dataBase;

    public ThesisEditPresenter(@NonNull ThesisEditContract.Router router,
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
    public void loadNewThesis(@NonNull String themeName) {
        try {
            List<Thesis> thesisList = new LoadThesisListTask(dataBase).execute(themeName)
                                                                      .get();

            Thesis thesis = thesisList.get(thesisList.size() - 1);

            Objects.requireNonNull(getView())
                   .showThesis(thesis);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "loadThesis: ", e);
        }
    }

    @Override
    public void saveThesis(@NonNull Thesis thesis) {
        new UpdateThesisTask(dataBase).execute(thesis);
    }

    private static class UpdateThesisTask extends AsyncTask<Thesis, Void, Void> {

        @NonNull
        private final AppDataBase dataBase;

        public UpdateThesisTask(@NonNull AppDataBase dataBase) {
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

        public LoadThesisTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Thesis doInBackground(Integer... integers) {
            return dataBase.thesisRepository()
                           .getThesis(integers[0]);
        }
    }

    private static class LoadThesisListTask extends AsyncTask<String, Void, List<Thesis>> {

        @NonNull
        private final AppDataBase dataBase;

        public LoadThesisListTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected List<Thesis> doInBackground(String... strings) {
            return dataBase.thesisRepository()
                           .getThesisList(strings[0]);
        }
    }
}
