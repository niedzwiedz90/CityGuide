package com.niedzwiecki.przemyslguide.ui.base

import android.databinding.BaseObservable
import android.os.Bundle

/**
 * Created by niedzwiedz on 29.06.17.
 */

open class BaseViewModel<T : Navigator> : BaseObservable(), ViewModel {

    internal var navigator: Navigator? = null
    private var context: T? = null

    val isNavigatorAttached: Boolean
        get() = navigator != null

    override fun attachNavigator(navigator: Navigator) {
        this.navigator = navigator
    }

    override fun detachNavigator() {
        navigator = null
    }

    override fun onActivityResult(navigator: Navigator, requestCode: Int, resultCode: Int, data: Bundle) {

    }

    override fun onDestroy() {

    }

    override fun onCreate() {

    }

    override fun saveInstanceState(bundle: Bundle) {

    }

    override fun restoreInstanceState(bundle: Bundle) {

    }

    override fun attachContext(context: Navigator) {
        this.context = context as T
    }

    fun getNavigator(): Navigator? {
        checkNavigatorAttached()
        return navigator
    }

    fun checkNavigatorAttached() {
        if (!isNavigatorAttached) throw MvpViewNotAttachedException()
    }

    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray, activity: BaseActivity) {

    }

    class MvpViewNotAttachedException : RuntimeException("Please call attachNavigator(Context) before" + " requesting data to the ViewModel")

}
