package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;
import android.util.Log;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Theme;
import com.gmail.ivan.synopsis.mvp.contracts.NewThemeDialogContract;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;

public class NewThemePresenter
        extends BasePresenter<NewThemeDialogContract.View, NewThemeDialogContract.Router>
        implements NewThemeDialogContract.Presenter {

    private static final String TAG = NewThemePresenter.class.getSimpleName();

    @NonNull
    private final AppDataBase dataBase;

    public NewThemePresenter(@NonNull NewThemeDialogContract.Router router,
                             @NonNull AppDataBase dataBase) {
        super(router);

        this.dataBase = dataBase;
    }

    @Override
    public void addTheme(@NonNull String themeName) {

        if (themeName.isEmpty()) {
            Objects.requireNonNull(getView())
                   .emptyTheme();
            return;
        }

        List<Theme> themeList = Collections.emptyList();

        try {
            themeList = new LoadThemeListTask(dataBase).execute().get();
        } catch (ExecutionException | InterruptedException e) {
            Log.e(TAG, "addTheme: ", e);
        }

        for(Theme theme: themeList){
            if(theme.getThemeName().equals(themeName)){
                Objects.requireNonNull(getView())
                       .alreadyExist();
                return;
            }
        }

        Theme theme = new Theme(themeName);
        new AddThemeTask(dataBase).execute(theme);
        Objects.requireNonNull(getView())
               .themeAdded();
    }

    private static class AddThemeTask extends AsyncTask<Theme, Void, Void> {

        @NonNull
        private final AppDataBase dataBase;

        AddThemeTask(@NonNull AppDataBase dataBase) {
            this.dataBase = dataBase;
        }

        @Override
        protected Void doInBackground(Theme... themes) {
            if (dataBase.themeRepository()
                        .getAllThemes()
                        .contains(themes[0])) {
                return null;
            }

            dataBase.themeRepository()
                    .addTheme(themes[0]);
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
