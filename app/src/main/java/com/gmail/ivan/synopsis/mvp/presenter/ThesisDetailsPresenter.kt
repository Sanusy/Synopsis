package com.gmail.ivan.synopsis.mvp.presenter

import com.gmail.ivan.synopsis.data.entity.Thesis
import com.gmail.ivan.synopsis.data.repository.ThesisRepository
import com.gmail.ivan.synopsis.mvp.contracts.ThesisDetailsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThesisDetailsPresenter(
    router: ThesisDetailsContract.Router,
    private val thesisRepository: ThesisRepository,
) : BasePresenter<ThesisDetailsContract.View, ThesisDetailsContract.Router>(router),
    ThesisDetailsContract.Presenter {

    private var thesis: Thesis? = null

    override fun loadThesis(thesisId: Int, isNewThesis: Boolean) {
        launch {
            thesis = withContext(Dispatchers.IO) {
                thesisRepository.getThesis(thesisId)
            }
            view?.loadThesis(thesis!!)
            if (!isNewThesis) {
                saveChanges()
            }
        }
    }

    override fun showEdit() {
        view?.showEditThesis()
    }

    override fun saveChanges() {
        launch {
            withContext(Dispatchers.IO) {
                thesisRepository.updateThesis(thesis!!)
            }
            view?.showThesisDetails()
        }
    }

    override fun showConfirmChanges() {
        router.showConfirmChanges()
    }

    companion object {

        private val TAG = ThesisDetailsPresenter::class.java.simpleName
    }
}