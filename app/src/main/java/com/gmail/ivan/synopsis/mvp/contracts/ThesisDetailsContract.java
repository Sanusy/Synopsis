package com.gmail.ivan.synopsis.mvp.contracts;

import com.gmail.ivan.synopsis.data.entity.Thesis;

import androidx.annotation.NonNull;

public interface ThesisDetailsContract {

    interface View extends BaseContract.View {

        void loadThesis(@NonNull Thesis thesis);

        void showThesisDetails();

        void showEditThesis();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void loadThesis(int thesisId);

        void showEdit();

        void saveChanges();

        void showConfirmChanges();
    }

    interface Router extends BaseContract.Router {

        void showConfirmChanges();
    }
}
