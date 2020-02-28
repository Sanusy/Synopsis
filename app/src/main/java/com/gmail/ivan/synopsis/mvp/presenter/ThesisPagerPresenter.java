package com.gmail.ivan.synopsis.mvp.presenter;

import android.os.AsyncTask;

import com.gmail.ivan.synopsis.data.database.AppDataBase;
import com.gmail.ivan.synopsis.data.entity.Thesis;
import com.gmail.ivan.synopsis.mvp.contracts.ThesisPagerContract;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;

public class ThesisPagerPresenter
        extends BasePresenter<ThesisPagerContract.View, ThesisPagerContract.Router>
        implements ThesisPagerContract.Presenter {

    @NonNull
    private final AppDataBase dataBase;

    @NonNull
    private final String themeName;

    public ThesisPagerPresenter(@NonNull ThesisPagerContract.Router router,
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
            Objects.requireNonNull(getView())
                   .setThesisList(thesisList);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
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
