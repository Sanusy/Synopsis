package com.gmail.ivan.synopsis.mvp.presenter

import androidx.annotation.CallSuper
import com.gmail.ivan.synopsis.mvp.contracts.BaseContract
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

abstract class BasePresenter<V : BaseContract.View, R : BaseContract.Router>(val router: R) :
    BaseContract.Presenter<V>, CoroutineScope {

    private var view: V? = null

    @CallSuper override fun attach(view: V) {
        this.view = view
    }

    @CallSuper override fun detach() {
        view = null
    }

    override fun back() {
        router!!.back()
    }

    override fun getView(): V? {
        return view
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}