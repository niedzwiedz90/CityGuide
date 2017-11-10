package com.niedzwiecki.przemyslguide.ui.main

import com.niedzwiecki.przemyslguide.data.DataManager
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.util.RxUtil

import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import timber.log.Timber

class MainViewModel(private val dataManager: DataManager) : BaseViewModel<MainActivity>() {

    private var mSubscription: Subscription? = null

    fun loadPlaces() {
        RxUtil.unsubscribe(mSubscription)
        mSubscription = dataManager.places
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(object : Subscriber<List<PlaceOfInterest>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(throwable: Throwable) {
                        Timber.e(throwable, "<---ERROR RESPONSE")
                        loadPlaces()
                    }

                    override fun onNext(places: List<PlaceOfInterest>) {
                        navigator.moveForward(Navigator.Options.SHOW_PLACES, places)
                    }
                })
    }


    fun logout() {
        dataManager.preferencesHelper.clearAuthenticationHeader(PreferencesKeys.LOGION_HEADER)
        navigator.moveForward(Navigator.Options.START_EMAIL_ACTIVITY)
        navigator.finish()
    }

}
