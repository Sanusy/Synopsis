package com.gmail.ivan.synopsis.mvp.presenter

import com.gmail.ivan.synopsis.data.entity.Theme
import com.gmail.ivan.synopsis.data.repository.ThemeRepository
import com.gmail.ivan.synopsis.mvp.contracts.NewThemeDialogContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewThemePresenter(
    router: NewThemeDialogContract.Router,
    private val themeRepository: ThemeRepository
) : BasePresenter<NewThemeDialogContract.View, NewThemeDialogContract.Router>(router),
    NewThemeDialogContract.Presenter {

    override fun addTheme(themeName: String) {
        if (themeName.isEmpty()) {
            view?.emptyTheme()
            return
        }

        launch {
            val themeList: List<Theme> = withContext(Dispatchers.IO) {
                themeRepository.getAllThemes()
            }
            for (theme in themeList) {
                if (theme.themeName == themeName) {
                    view?.alreadyExist()
                    return@launch
                }
            }
            val theme = Theme(themeName)
            withContext(Dispatchers.IO) {
                themeRepository.addTheme(theme)
            }
            view?.themeAdded()
        }
    }

    companion object {

        private val TAG = NewThemePresenter::class.java.simpleName
    }
}