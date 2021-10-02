package com.gmail.ivan.synopsis.mvp.presenter

import com.gmail.ivan.synopsis.mvp.contracts.SaveEditContract

class SaveEditPresenter(router: SaveEditContract.Router) :
    BasePresenter<SaveEditContract.View, SaveEditContract.Router>(router),
    SaveEditContract.Presenter {

    override fun saveChanges() {
        view?.saveChanges()
    }

    override fun discardChanges() {
        view?.discardChanges()
    }
}