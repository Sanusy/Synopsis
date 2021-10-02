package com.gmail.ivan.synopsis.mvp.presenter

import com.gmail.ivan.synopsis.data.entity.Thesis
import com.gmail.ivan.synopsis.data.repository.ThesisRepository
import com.gmail.ivan.synopsis.mvp.contracts.NewThesisContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewThesisPresenter(
    router: NewThesisContract.Router,
    private val thesisRepository: ThesisRepository,
    private val themeName: String
) : BasePresenter<NewThesisContract.View, NewThesisContract.Router>(router),
    NewThesisContract.Presenter {

    override fun addThesis(title: String) {
        if (title.isEmpty()) {
            view?.emptyTitle()
            return
        }
        val thesis = Thesis(themeName, title)

        launch {
            withContext(Dispatchers.IO) {
                thesisRepository.addThesis(thesis)
            }
            view?.thesisAdded()

            router!!.openNewThesis(thesis)
        }
    }
}