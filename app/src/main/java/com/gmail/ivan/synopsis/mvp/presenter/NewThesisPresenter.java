package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.NewThesisContract;

import java.util.Objects;

import androidx.annotation.NonNull;

public class NewThesisPresenter
        extends BasePresenter<NewThesisContract.View, NewThesisContract.Router>
        implements NewThesisContract.Presenter {

    @NonNull
    private final AppDataBase dataBase;

    @NonNull
    private final String themeName;

    public NewThesisPresenter(@NonNull NewThesisContract.Router router,
                              @NonNull AppDataBase dataBase,
                              @NonNull String themeName) {
        super(router);

        this.dataBase = dataBase;
        this.themeName = themeName;
    }

    @Override
    public void addThesis(@NonNull String title) {
        if (title.isEmpty()) {
            Objects.requireNonNull(getView())
                   .emptyTitle();
            return;
        }
        Thesis thesis = new Thesis(themeName, title);
        new AddThesisTask(dataBase).execute(thesis);
        Objects.requireNonNull(getView())
               .thesisAdded();
        getRouter().openNewThesis(thesis);
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
}
