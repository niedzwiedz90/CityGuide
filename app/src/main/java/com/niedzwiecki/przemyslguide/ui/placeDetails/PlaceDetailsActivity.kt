package com.niedzwiecki.przemyslguide.ui.placeDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.databinding.ActivityPlaceDetailsBinding
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.ui.base.ViewModel
import com.niedzwiecki.przemyslguide.ui.main.MainActivity
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity

/**
 * Created by niedzwiedz on 10.07.17.
 */

class PlaceDetailsActivity : BaseActivity() {

    private var place: PlaceOfInterest? = null

    override fun beforeViews() {
        super.beforeViews()
        setDataBindingEnabled(true)
    }

    override fun afterViews() {
        super.afterViews()
        viewDataBinding.viewModel = getViewModel()
        setScreenFlags()
        fetchData()
        setDataToViewModel()
    }

    private fun fetchData() {
        if (intent.hasExtra(MainActivity.INTEREST_PLACE_KEY)) {
            place = intent.extras!!.getParcelable(MainActivity.INTEREST_PLACE_KEY)
        }
    }

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)
        viewDataBinding.viewModel = getViewModel()
        setScreenFlags()
        fetchData()
        setDataToViewModel()
        if (savedInstanceState != null) {
            restoreData(savedInstanceState)
        }
    }

    private fun restoreData(savedInstanceState: Bundle) {
        place = savedInstanceState.getParcelable(INTEREST_PLACE_KEY)
    }

    private fun setScreenFlags() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }

    private fun setDataToViewModel() {
        getViewModel().setData(place)
    }

    override fun contentId(): Int {
        return R.layout.activity_place_details
    }

    override fun getViewDataBinding(): ActivityPlaceDetailsBinding {
        return (super.getViewDataBinding() as? ActivityPlaceDetailsBinding)!!
    }

    override fun createViewModel(): ViewModel {
        return PlaceDetailsViewModel(dataManager)
    }

    override fun getViewModel(): PlaceDetailsViewModel {
        return super.getViewModel() as PlaceDetailsViewModel
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(INTEREST_PLACE_KEY, place)
    }

    override fun moveForward(options: Navigator.Options?, vararg data: Any?) {
        super.moveForward(options, *data)
        when (options) {
            Navigator.Options.START_ACTIVITY_WITH_INTENT ->
                startMapActivity(data[0] as PlaceOfInterest)
        }

    }

    private fun startMapActivity(place: PlaceOfInterest) {
        val intent = Intent(MapsActivity.getStartIntent(baseContext, place))
        startActivity(intent)
    }

    companion object {

        private val INTEREST_PLACE_KEY = "interestPlaceKey"

        fun getStartIntent(context: Context, place: PlaceOfInterest): Intent {
            val intent = Intent(context, PlaceDetailsActivity::class.java)
            intent.putExtra(MainActivity.INTEREST_PLACE_KEY, place)
            return intent
        }
    }
}
