package com.gmail.ivan.synopsis.mvp.presenter

import com.gmail.ivan.synopsis.data.entity.Thesis
import com.gmail.ivan.synopsis.data.repository.ThesisRepository
import com.gmail.ivan.synopsis.mvp.contracts.ThesisListContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThesisListPresenter(
    router: ThesisListContract.Router,
    private val thesisRepository: ThesisRepository,
    private val themeName: String
) : BasePresenter<ThesisListContract.View, ThesisListContract.Router>(router),
    ThesisListContract.Presenter {

    private var recentlyDeletedThesis: Thesis? = null

    override fun loadThesisList() {
        launch {
            val thesisList: List<Thesis> = withContext(Dispatchers.IO) {
                thesisRepository.getThesisList(themeName)
            }
            if (thesisList.isEmpty()) {
                view?.showEmptyThesisList()
            } else {
                view?.showThesisList(thesisList)
            }
        }
    }

    override fun newThesis() {
        router.openNewThesis(themeName)
    }

    override fun openThesis(thesis: Thesis) {
        router.openThesis(thesis)
    }

    override fun deleteThesis(thesis: Thesis) {
        recentlyDeletedThesis = thesis
        launch {
            withContext(Dispatchers.IO) {
                thesisRepository.deleteThesis(thesis)
            }
            view?.showUndoDelete()
        }
    }

    override fun addRecentlyDeleted() {
        launch {
            val thesisList = withContext(Dispatchers.IO) {
                thesisRepository.addThesis(recentlyDeletedThesis!!)
                thesisRepository.getThesisList(themeName)
            }
            view?.showThesisList(thesisList)
        }
    }

    companion object {

        private val TAG = ThesisListPresenter::class.java.simpleName
    }
}