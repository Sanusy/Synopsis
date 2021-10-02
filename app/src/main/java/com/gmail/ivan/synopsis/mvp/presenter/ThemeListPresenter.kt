package com.gmail.ivan.synopsis.mvp.presenter

import com.gmail.ivan.synopsis.data.entity.Theme
import com.gmail.ivan.synopsis.data.repository.ThemeRepository
import com.gmail.ivan.synopsis.data.repository.ThesisRepository
import com.gmail.ivan.synopsis.mvp.contracts.ThemeListContract
import com.gmail.ivan.synopsis.ui.router.ThemeListRouter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ThemeListPresenter(
    router: ThemeListRouter,
    private val themeRepository: ThemeRepository,
    private val thesisRepository: ThesisRepository,
) :
    BasePresenter<ThemeListContract.View, ThemeListContract.Router>(router),
    ThemeListContract.Presenter {

    private var recentlyDeletedTheme: Theme? = null

    override fun loadThemeList() {
        launch {
            val themeList: List<Theme> = withContext(Dispatchers.IO) {
                val retreivedThemeList = themeRepository.getAllThemes()
                for (theme in retreivedThemeList) {
                    theme.thesisCount = thesisRepository
                        .getThesisList(theme.themeName)
                        .size
                }
                retreivedThemeList
            }

            if (themeList.isEmpty()) {
                view?.showEmptyList()
            } else {
                view?.showThemeList(themeList)
            }
        }
    }

    override fun newTheme() {
        router.openNewTheme()
    }

    override fun openTheme(theme: Theme) {
        router.openTheme(theme)
    }

    override fun delete(theme: Theme) {
        recentlyDeletedTheme = theme
        launch {
            withContext(Dispatchers.IO) {
                themeRepository.deleteTheme(theme)
            }
            view?.showUndoDelete()
        }
    }

    override fun addRecentlyDeleted() {
        launch {
            val themeList = withContext(Dispatchers.IO) {
                themeRepository.addTheme(recentlyDeletedTheme!!)
                val themeList: List<Theme> = themeRepository.getAllThemes()
                for (theme in themeList) {
                    theme.thesisCount = thesisRepository
                        .getThesisList(theme.themeName)
                        .size
                }
                themeList
            }
            view?.showThemeList(themeList)
        }
    }

    companion object {

        private val TAG = ThemeListPresenter::class.java.simpleName
    }
}