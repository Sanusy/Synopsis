package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Theme;
import com.gmail.ivan.synopsis.mvp.contracts.NewThemeDialogContract;

import java.util.Objects;

import androidx.annotation.NonNull;

public class NewThemePresenter
        extends BasePresenter<NewThemeDialogContract.View, NewThemeDialogContract.Router>
        implements NewThemeDialogContract.Presenter {

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

        Theme theme = new Theme(themeName);
        new AddThemeTask(dataBase).execute(theme);
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
}
