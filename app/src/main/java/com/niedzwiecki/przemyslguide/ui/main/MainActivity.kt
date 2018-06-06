package com.niedzwiecki.przemyslguide.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.widget.TextView
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.SyncService
import com.niedzwiecki.przemyslguide.data.model.InterestPlace
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.databinding.ActivityMainBinding
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.ui.base.ViewModel
import com.niedzwiecki.przemyslguide.ui.login.email.EmailActivity
import com.niedzwiecki.przemyslguide.ui.login.password.PasswordActivity.Companion.EMAIL_KEY
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity.Companion.PLACES_LIST
import com.niedzwiecki.przemyslguide.ui.placeDetails.PlaceDetailsActivity
import com.niedzwiecki.przemyslguide.util.RecyclerItemClickListener

class MainActivity : BaseActivity() {

    private var email: String? = null
    private var placesList: List<PlaceOfInterest>? = null
    private lateinit var header: TextView

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
        fetchExtraData()
        init()
        getViewModel().loadPlaces()
    }

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)
        if (savedInstanceState != null) {
            placesList = savedInstanceState.get(INTEREST_PLACE_KEY) as List<PlaceOfInterest>?
            init()
            showPlaces(placesList)
            getViewModel().setPlaceList(placesList)
        }
    }

    private fun fetchExtraData() {
        if (intent.hasExtra(EMAIL_KEY)) {
            email = intent.getStringExtra(EMAIL_KEY)
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

    private lateinit var placesAdapter: PlacesAdapter

    private fun init() {
        setSupportActionBar(viewDataBinding.toolbar)
        placesAdapter = PlacesAdapter()
        viewDataBinding.recyclerView.adapter = placesAdapter
        viewDataBinding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        viewDataBinding.recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(
                        this, viewDataBinding.recyclerView,
                        object : RecyclerItemClickListener.OnItemClickListener {
                            override fun onItemClick(view: View, position: Int) {
                                val places = placesAdapter.getPlace(position)
                                openDetail(places!!)
                            }

                            override fun onLongItemClick(view: View, position: Int) {

                            }
                        }))

//        header = viewDataBinding.navView.getHeaderView(0).findViewById(R.id.emailInfo) as TextView
//        header = viewDataBinding.navView.getHeaderView(0).findViewById(R.id.emailInfo) as TextView
//        if (!Utils.isEmpty(email)) {
//            header.setText(email)
//        }

        viewDataBinding.recyclerView.layoutManager = GridLayoutManager(this, 2)
        val toggle = ActionBarDrawerToggle(
                this,
                viewDataBinding.drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        )

        viewDataBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        viewDataBinding.navView.setNavigationItemSelectedListener { item ->
            viewDataBinding.drawerLayout.closeDrawers()
            when (item.itemId) {
                R.id.navMap -> {
                    getViewModel().filterPlaces("all")
                    true
                }

                R.id.navMapWithHotels -> {
                    getViewModel().filterPlaces("hotel")
                    true
                }

                R.id.navMapWithCastles -> {
                    getViewModel().filterPlaces("castle")
                    true
                }
                R.id.navMapWithFort -> {
                    getViewModel().filterPlaces("station")
                    true
                }
                R.id.navMapWithStation -> {
                    getViewModel().filterPlaces("station")
                    true
                }
                R.id.navMapWithBunker -> {
                    getViewModel().filterPlaces("bunker")
                    true
                }
                R.id.navLogout -> {
                    viewDataBinding.viewModel?.logout()
                    viewDataBinding.viewModel.navigator?.showProgress("PROCESSING")
                    true
                }
                else -> true
            }
        }

        if (!getViewModel().isRefreshing.get()) {
            viewDataBinding.swipeToRefresh.setOnRefreshListener { viewDataBinding.viewModel?.loadPlaces() }
        }
    }

    private lateinit var savedListOfPlaces: ArrayList<PlaceOfInterest>

    private fun savePlacesList(placesList: List<PlaceOfInterest>) {
        savedListOfPlaces = ArrayList<PlaceOfInterest>()
        for (interestPlace in placesList) {
            savedListOfPlaces.add(interestPlace)
        }
    }

    private fun openMapWithFilterPlaces(filteredInterestPlacesList: ArrayList<InterestPlace>) {
        val intent = Intent(this@MainActivity, MapsActivity::class.java)
        intent.putExtra(PLACES_LIST, filteredInterestPlacesList)
        startActivity(intent)
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
            Navigator.Options.SHOW_PLACES -> {
                if (viewDataBinding.swipeToRefresh.isRefreshing) {
                    viewDataBinding.swipeToRefresh.isRefreshing =
                            !viewDataBinding.swipeToRefresh.isRefreshing
                }

                placesList = data[0] as List<PlaceOfInterest>
                showPlaces(placesList)
            }

            Navigator.Options.START_EMAIL_ACTIVITY -> EmailActivity.start(this)
            Navigator.Options.SHOW_FILTERED_PLACES -> openMapWithFilterPlaces(data[0] as ArrayList<InterestPlace>)
        }
    }

    fun showPlaces(interestPlaces: List<PlaceOfInterest>?) {
        placesAdapter.setPlaces(interestPlaces)
        placesAdapter.notifyDataSetChanged()
        if (interestPlaces != null) {
            savePlacesList(interestPlaces)
        }

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putParcelableArrayList(INTEREST_PLACE_KEY, savedListOfPlaces)
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
