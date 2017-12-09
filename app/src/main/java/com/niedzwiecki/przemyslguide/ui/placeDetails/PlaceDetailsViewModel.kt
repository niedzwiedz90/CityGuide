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
    var placeOfIntrest: PlaceOfInterest? = null

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

        if (!Utils.isEmpty(place?.image))
            imageFieldUrl.set(place?.image)

        emailFieldVisibility.set(View.GONE)
    }

    fun onMapButtonClick(view: View) {
        navigator?.moveForward(Navigator.Options.START_ACTIVITY_WITH_INTENT, placeOfIntrest)
    }
}