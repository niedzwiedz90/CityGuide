package com.niedzwiecki.przemyslguide.ui.placeDetails

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.view.View
import com.niedzwiecki.przemyslguide.data.DataManager
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.util.Utils

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

class PlaceDetailsViewModel(var DataManager: DataManager) : BaseViewModel<PlaceDetailsActivity>() {

    val name = ObservableField<String>()
    val placeDescription = ObservableField<String>()
    val placeNameFieldVisibility = ObservableInt()
    val placeDescriptionFieldVisibility = ObservableInt()
    val emailFieldVisibility = ObservableInt()
    val imageFieldUrl = ObservableField<String>()
    val emailField = ObservableField<String>()
    var placeOfIntrest: PlaceOfInterest? = null
    val telephoneFieldVisibility = ObservableInt()
    val telephoneField = ObservableField<String>()
    val locationField = ObservableField<String>()
    val typeFieldVisibility = ObservableInt()
    val mapSectionVisibility = ObservableInt()
    val gallerySectionVisibility = ObservableInt()

    fun setData(place: PlaceOfInterest?) {
        placeOfIntrest = place

        if (Utils.isEmpty(place?.name)) {
            placeNameFieldVisibility.set(View.GONE)
        } else {
            name.set(place?.name)
        }

        if (Utils.isEmpty(place?.description)) {
            placeDescriptionFieldVisibility.set(View.GONE)
        } else {
            placeDescription.set(place?.description)
        }

        if (!Utils.isEmpty(place?.image)) {
            imageFieldUrl.set(place?.image)
        }

        if (Utils.isEmpty(place?.email)) {
            emailFieldVisibility.set(View.GONE)
        } else {
            emailField.set(place?.email)
        }

        if (Utils.isEmpty(place?.telephone)) {
            telephoneFieldVisibility.set(View.GONE)
        } else {
            telephoneField.set(place?.telephone)
        }

        if (Utils.isEmpty(place?.type)) {
            typeFieldVisibility.set(View.GONE)
        } else {
            locationField.set(place?.type)
        }

        if (Utils.isEmpty(place?.images)) {
            gallerySectionVisibility.set(View.GONE)
        } else {
            gallerySectionVisibility.set(View.VISIBLE)
        }
    }

    fun onMapButtonClick(view: View) {
        navigator?.moveForward(Navigator.Options.START_ACTIVITY_WITH_INTENT, placeOfIntrest)
    }

    fun onFabButtonClick(view: View) {
        navigator?.moveForward(Navigator.Options.START_SLIDER_ACTIVITY)
    }
}