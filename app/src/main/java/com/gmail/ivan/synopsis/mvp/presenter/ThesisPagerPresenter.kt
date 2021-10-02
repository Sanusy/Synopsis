package com.gmail.ivan.synopsis.mvp.presenter

import com.gmail.ivan.synopsis.data.entity.Thesis
import com.gmail.ivan.synopsis.data.repository.ThesisRepository
import com.gmail.ivan.synopsis.mvp.contracts.ThesisPagerContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThesisPagerPresenter(
    router: ThesisPagerContract.Router,
    private val thesisRepository: ThesisRepository,
    private val themeName: String
) : BasePresenter<ThesisPagerContract.View, ThesisPagerContract.Router>(router),
    ThesisPagerContract.Presenter {

    override fun loadThesisList() {
        launch {
            val thesisList: List<Thesis> = withContext(Dispatchers.IO) {
                thesisRepository.getThesisList(themeName)
            }
            view?.setThesisList(thesisList)
        }
    }
}