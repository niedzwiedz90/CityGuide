package com.niedzwiecki.przemyslguide.ui.main

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.SyncService
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.databinding.ActivityMainBinding
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.ui.base.ViewModel
import com.niedzwiecki.przemyslguide.ui.login.email.EmailActivity
import com.niedzwiecki.przemyslguide.ui.login.password.PasswordActivity.EMAIL_KEY
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity.PLACES_LIST
import com.niedzwiecki.przemyslguide.ui.placeDetails.PlaceDetailsActivity
import com.niedzwiecki.przemyslguide.util.RecyclerItemClickListener
import java.util.*

class MainActivity : BaseActivity() {

    private var email: String? = null
    private var placesList: List<PlaceOfInterest>? = null

    override fun beforeViews() {
        super.beforeViews()
        setDataBindingEnabled(true)
    }

    override fun contentId(): Int {
        return R.layout.activity_main
    }

    override fun afterViews() {
        super.afterViews()
        viewDataBinding.viewModel = getViewModel()
        fetchData()
        init()
    }

    private fun fetchData() {
        if (intent.hasExtra(EMAIL_KEY)) {
            email = intent.getStringExtra(EMAIL_KEY)

            //            View header = navigationView.getHeaderView(0);
            //            TextView name = (TextView) header.findViewById(R.id.emailInfo);
            //            name.setText(email);
        }

        if (intent.getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this))
        }

    }

    override fun createViewModel(): ViewModel {
        return MainViewModel(dataManager)
    }

    override fun getViewModel(): MainViewModel {
        return super.getViewModel() as MainViewModel
    }

    override fun getViewDataBinding(): ActivityMainBinding {
        return super.getViewDataBinding() as ActivityMainBinding
    }

    private fun init() {
        getViewModel().loadPlaces()
        viewDataBinding.recyclerView.layoutManager = GridLayoutManager(this, 2)

        viewDataBinding.navView.setNavigationItemSelectedListener { item ->
            /*  if (item.isChecked()) {
                            item.setChecked(false);
                        } else {
                            item.setChecked(true);
                        }*/

            viewDataBinding.drawerLayout.closeDrawers()

            when (item.itemId) {
                R.id.navMap -> {
                    filterPlaces("all")
                    true
                }

                R.id.navMapWithHotels -> {
                    filterPlaces("hotel")
                    true
                }
                R.id.navMapWithCastles -> {
                    filterPlaces("castle")
                    true
                }
                R.id.navMapWithFort -> {
                    filterPlaces("station")
                    true
                }
                R.id.navMapWithStation -> {
                    filterPlaces("station")
                    true
                }
                R.id.navMapWithBunker -> {
                    filterPlaces("bunker")
                    true
                }
                R.id.navLogout -> {
                    viewDataBinding.viewModel?.logout()
                    true
                }
                else -> true
            }
        }

        if (!getViewModel().isRefreshing.get()) {
            viewDataBinding.swipeToRefresh.setOnRefreshListener { viewDataBinding.viewModel?.loadPlaces() }
        }
    }

    private fun filterPlaces(type: String) {
        if (placesList != null) {
            val tempList = ArrayList<PlaceOfInterest>()
            for (interestPlace in placesList!!) {
                if (interestPlace.type == type) {
                    tempList.add(interestPlace)
                } else if (type == "all") {
                    tempList.add(interestPlace)
                }
            }

            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            intent.putExtra(PLACES_LIST, tempList)
            startActivity(intent)
        }
    }

    private fun openDetail(interestPlace: PlaceOfInterest) {
        startActivity(PlaceDetailsActivity.getStartIntent(this, interestPlace))
    }


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (viewModel != null) {
            viewModel.attachNavigator(this)
        }
    }

    override fun moveForward(options: Navigator.Options, vararg data: Any) {
        super.moveForward(options, *data)
        when (options) {
           /* Navigator.Options.SHOW_PLACES -> {
                if (viewDataBinding.swipeToRefresh.isRefreshing) {
                    viewDataBinding.swipeToRefresh.isRefreshing = !viewDataBinding.swipeToRefresh.isRefreshing
                }

                placesList = data[0] as List<PlaceOfInterest>
                showPlaces(placesList)
            }*/
            Navigator.Options.START_EMAIL_ACTIVITY -> EmailActivity.start(this)
        }
    }

    //MVP
    fun showPlaces(interestPlaces: List<PlaceOfInterest>?) {
        getViewModel().placesAdapter?.setPlaces(interestPlaces)
        getViewModel().placesAdapter?.notifyDataSetChanged()
    }

    companion object {

        private val EXTRA_TRIGGER_SYNC_FLAG = "uk.co.ribot.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG"

        val INTEREST_PLACE_KEY = "com.niedzwiecki.przemyslGuide.PlaceDetailActivity.key"

        fun start(context: Activity, email: String) {
            val starter = Intent(context, MainActivity::class.java)
            starter.putExtra(EMAIL_KEY, email)
            context.startActivity(starter)
            context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }

}
