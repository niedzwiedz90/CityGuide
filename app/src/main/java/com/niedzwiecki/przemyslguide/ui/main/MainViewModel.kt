package com.niedzwiecki.przemyslguide.ui.main

import android.databinding.ObservableBoolean
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
import java.util.*

class MainViewModel(private val dataManager: DataManager) : BaseViewModel<MainActivity>() {

    private var mSubscription: Subscription? = null
    private lateinit var placesList: List<PlaceOfInterest>
    val isRefreshing = ObservableBoolean(false)

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
                        placesList = places
                        isRefreshing.set(false)
                        isRefreshing.notifyChange()
                    }
                })
    }

    fun logout() {
        dataManager.preferencesHelper.clearAuthenticationHeader(PreferencesKeys.LOGION_HEADER)
        navigator.moveForward(Navigator.Options.START_EMAIL_ACTIVITY)
        navigator.finish()
    }

    fun filterPlaces(type: String) {
        val tempList = ArrayList<PlaceOfInterest>()
        for (interestPlace in placesList) {
            if (interestPlace.type == type) {
                tempList.add(interestPlace)
            } else if (type == "all") {
                tempList.add(interestPlace)
            }
        }

        navigator.moveForward(Navigator.Options.SHOW_FILTERED_PLACES, tempList)
    }

    fun setPlaceList(restoredPlacesList: List<PlaceOfInterest>?) {
        this.placesList = restoredPlacesList!!
    }

}
