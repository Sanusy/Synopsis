package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisListContract;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThesisListPresenter
        extends BasePresenter<ThesisListContract.View, ThesisListContract.Router>
        implements ThesisListContract.Presenter {

    private static final String TAG = ThesisListPresenter.class.getSimpleName();

    @NonNull
    private final AppDataBase dataBase;

    @NonNull
    private final String themeName;

    @Nullable
    private Thesis recentlyDeletedThesis;

    public ThesisListPresenter(@NonNull ThesisListContract.Router router,
                               @NonNull AppDataBase dataBase,
                               @NonNull String themeName) {
        super(router);

        this.dataBase = dataBase;
        this.themeName = themeName;
    }

    @Override
    public void loadThesisList() {
        try {
            List<Thesis> thesisList = new LoadThesisListTask(dataBase).execute(themeName)
                                                                      .get();
            if (thesisList.isEmpty()) {
                Objects.requireNonNull(getView())
                       .showEmptyThesisList();
            } else {
                Objects.requireNonNull(getView())
                       .showThesisList(thesisList);
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "loadThesisList: ", e);
        }
    }

    @Override
    public void newThesis() {
        getRouter().openNewThesis(themeName);
    }

    @Override
    public void openThesis(@NonNull Thesis thesis) {
        getRouter().openThesis(thesis);
    }

    @Override
    public void deleteThesis(@NonNull Thesis thesis) {
        recentlyDeletedThesis = thesis;
        new DeleteThesisTask(dataBase).execute(thesis);
        Objects.requireNonNull(getView())
               .showUndoDelete();
    }

    @Override
    public void addRecentlyDeleted() {
        new AddThesisTask(dataBase).execute(recentlyDeletedThesis);
        try {
            List<Thesis> thesisList = new LoadThesisListTask(dataBase).execute(themeName)
                                                                      .get();

            Objects.requireNonNull(getView())
                   .showThesisList(thesisList);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "loadThemeList: ", e);
        }
    }

    private static class DeleteThesisTask extends AsyncTask<Thesis, Void, Void> {

        @NonNull
        private final AppDataBase dataBase;

        DeleteThesisTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Void doInBackground(Thesis... theses) {
            dataBase.thesisRepository()
                    .deleteThesis(theses[0]);
            return null;
        }
    }

    private static class AddThesisTask extends AsyncTask<Thesis, Void, Void> {

        @NonNull
        private final AppDataBase dataBase;

        AddThesisTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Void doInBackground(Thesis... theses) {
            dataBase.thesisRepository()
                    .addThesis(theses[0]);
            return null;
        }
    }

    private static class LoadThesisListTask extends AsyncTask<String, Void, List<Thesis>> {

        @NonNull
        private final AppDataBase dataBase;

        LoadThesisListTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected List<Thesis> doInBackground(String... strings) {
            return dataBase.thesisRepository()
                           .getThesisList(strings[0]);
        }
    }
}
