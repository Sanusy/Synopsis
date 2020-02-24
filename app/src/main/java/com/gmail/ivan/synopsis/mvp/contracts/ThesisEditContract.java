package com.gmail.ivan.synopsis.mvp.contracts;

import com.gmail.ivan.synopsis.data.entity.Thesis;

import androidx.annotation.NonNull;

public interface ThesisEditContract {

    interface View extends BaseContract.View {

        void showThesis(@NonNull Thesis thesis);
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void loadThesis(int thesisId);

        void saveThesis(@NonNull Thesis thesis);

        void loadNewThesis(@NonNull String themeName);
    }

    interface Router extends BaseContract.Router {

    }
}
