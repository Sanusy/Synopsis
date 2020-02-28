package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Theme;
import com.gmail.ivan.synopsis.mvp.contracts.ThemeListContract;
import com.gmail.ivan.synopsis.ui.router.ThemeListRouter;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ThemeListPresenter
        extends BasePresenter<ThemeListContract.View, ThemeListContract.Router> implements
                                                                                ThemeListContract.Presenter {

    private static final String TAG = ThemeListPresenter.class.getSimpleName();

    @NonNull
    private final AppDataBase dataBase;

    @Nullable
    private Theme recentlyDeletedTheme;

    public ThemeListPresenter(@NonNull ThemeListRouter router, @NonNull AppDataBase dataBase) {
        super(router);

        this.dataBase = dataBase;
    }

    @Override
    public void loadThemeList() {
        try {
            List<Theme> themeList = new LoadThemeListTask(dataBase).execute()
                                                                   .get();
            if (themeList.isEmpty()) {
                Objects.requireNonNull(getView())
                       .showEmptyList();
            } else {
                Objects.requireNonNull(getView())
                       .showThemeList(themeList);
            }
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "loadThemeList: ", e);
        }
    }

    @Override
    public void newTheme() {
        getRouter().openNewTheme();
    }

    @Override
    public void openTheme(@NonNull Theme theme) {
        getRouter().openTheme(theme);
    }

    @Override
    public void delete(@NonNull Theme theme) {
        recentlyDeletedTheme = theme;
        new DeleteThemeTask(dataBase).execute(theme);
        Objects.requireNonNull(getView())
               .showUndoDelete();
    }

    @Override
    public void addRecentlyDeleted() {
        new AddRecentlyDeletedTask(dataBase).execute(recentlyDeletedTheme);
        try {
            List<Theme> themeList = new LoadThemeListTask(dataBase).execute()
                                                                   .get();

            Objects.requireNonNull(getView())
                   .showThemeList(themeList);
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "loadThemeList: ", e);
        }
    }

    private static class AddRecentlyDeletedTask extends AsyncTask<Theme, Void, Void> {

        @NonNull
        private final AppDataBase dataBase;

        AddRecentlyDeletedTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Void doInBackground(Theme... themes) {
            dataBase.themeRepository()
                    .addTheme(themes[0]);
            return null;
        }
    }

    private static class DeleteThemeTask extends AsyncTask<Theme, Void, Void> {

        @NonNull
        private final AppDataBase dataBase;

        DeleteThemeTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Void doInBackground(Theme... themes) {
            dataBase.themeRepository()
                    .deleteTheme(themes[0]);
            return null;
        }
    }

    private static class LoadThemeListTask extends AsyncTask<Void, Void, List<Theme>> {

        @NonNull
        private final AppDataBase dataBase;

        LoadThemeListTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected List<Theme> doInBackground(Void... voids) {
            List<Theme> themeList = dataBase.themeRepository()
                                            .getAllThemes();

            for (Theme theme : themeList) {
                theme.setThesisCount(dataBase.thesisRepository()
                                             .getThesisList(theme.getThemeName())
                                             .size());
            }

            return themeList;
        }
    }
}
