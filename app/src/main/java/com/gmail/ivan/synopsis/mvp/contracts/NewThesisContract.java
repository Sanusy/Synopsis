package com.gmail.ivan.synopsis.mvp.contracts;

import com.gmail.ivan.synopsis.data.entity.Thesis;

import androidx.annotation.NonNull;

public interface NewThesisContract {

    interface View extends BaseContract.View {

        void emptyTitle();

        void thesisAdded();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void addThesis(@NonNull String title);
    }

    interface Router extends BaseContract.Router {

        void openNewThesis(@NonNull Thesis thesis);
    }
}
