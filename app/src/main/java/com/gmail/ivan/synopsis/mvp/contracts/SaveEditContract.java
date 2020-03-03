package com.gmail.ivan.synopsis.mvp.contracts;

public interface SaveEditContract {

    interface View extends BaseContract.View {

        void saveChanges();

        void discardChanges();
    }

    interface Presenter extends BaseContract.Presenter<View> {

        void saveChanges();

        void discardChanges();
    }

    interface Router extends BaseContract.Router {

    }
}
