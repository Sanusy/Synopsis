package com.gmail.ivan.synopsis.mvp.presenter;

import com.gmail.ivan.synopsis.mvp.contracts.SaveEditContract;

import java.util.Objects;

import androidx.annotation.NonNull;

public class SaveEditPresenter
        extends BasePresenter<SaveEditContract.View, SaveEditContract.Router>
        implements SaveEditContract.Presenter {

    public SaveEditPresenter(@NonNull SaveEditContract.Router router) {
        super(router);
    }

    @Override
    public void saveChanges() {
        Objects.requireNonNull(getView())
               .saveChanges();
    }

    @Override
    public void discardChanges() {
        Objects.requireNonNull(getView())
               .discardChanges();
    }
}
