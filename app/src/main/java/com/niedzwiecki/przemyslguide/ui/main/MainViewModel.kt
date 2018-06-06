package com.niedzwiecki.przemyslguide.ui.main

import android.databinding.ObservableBoolean
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.DataManager
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.util.RxUtil
import rx.Subscriber
import rx.Subscription
import java.util.*

class MainViewModel(private val dataManager: DataManager) : BaseViewModel<MainActivity>() {

    private var mSubscription: Subscription? = null
    private lateinit var placesList: List<PlaceOfInterest>
    val isRefreshing = ObservableBoolean(false)
    val adapter = PlacesAdapter()

    fun loadPlaces() {
        RxUtil.unsubscribe(mSubscription)
        navigator?.showProgress(dataManager.resourcesManager.getString(R.string.processing))
        mSubscription = dataManager.places
                .subscribe(object : Subscriber<List<PlaceOfInterest>>() {
                    override fun onCompleted() {

                    }

                    override fun onError(throwable: Throwable) {
                        navigator?.showError(throwable.message.toString())
                        navigator?.hideProgress()
                        stopRefreshing()
                    }

                    override fun onNext(places: List<PlaceOfInterest>) {
                        navigator?.hideProgress()
                        navigator?.moveForward(Navigator.Options.SHOW_PLACES, places)
                        placesList = places
                        stopRefreshing()
                    }
                })

    }

    private fun stopRefreshing() {
        isRefreshing.set(false)
        isRefreshing.notifyChange()
    }

    fun logout() {
        dataManager.preferencesHelper.clearAuthenticationHeader(PreferencesKeys.LOGION_HEADER)
        navigator?.moveForward(Navigator.Options.START_EMAIL_ACTIVITY)
        navigator?.finish()
    }

    fun filterPlaces(type: String) {
        val tempList = ArrayList<PlaceOfInterest>()
        for (interestPlace in placesList) {
            if (interestPlace.type == type) {
                tempList.add(interestPlace)
            } else if (type == MainActivity.ALL_TYPE) {
                tempList.add(interestPlace)
            }
        }

        navigator?.moveForward(Navigator.Options.SHOW_FILTERED_PLACES, tempList)
    }

    fun setPlaceList(restoredPlacesList: List<PlaceOfInterest>?) {
        this.placesList = restoredPlacesList!!
    }

}
