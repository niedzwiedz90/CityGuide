package com.niedzwiecki.przemyslguide.ui.placeDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.AppCompatImageView
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.widget.TextView

import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.ui.PlaceDetailViewPager
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity
import com.niedzwiecki.przemyslguide.ui.main.MainActivity
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity
import com.niedzwiecki.przemyslguide.util.Utils
import com.squareup.picasso.Picasso

import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

/**
 * Created by niedzwiedz on 10.07.17.
 */

class PlaceDetailsActivity : BaseActivity() {

    @BindView(R.id.nameOfRibot)
    internal var nameTextView: TextView? = null

    @BindView(R.id.description)
    internal var descriptionTextView: TextView? = null

    @BindView(R.id.coverImage)
    internal var coverImage: AppCompatImageView? = null

    @BindView(R.id.mailTextView)
    internal var mailTextView: TextView? = null

    @BindView(R.id.viewPager)
    internal var viewPager: ViewPager? = null

    private var place: PlaceOfInterest? = null
    private var adapter: PlaceDetailViewPager? = null

    override fun beforeViews() {
        super.beforeViews()
        activityComponent().inject(this)
    }

    override fun afterViews() {
        super.afterViews()
        ButterKnife.bind(this)
        setScreenFlags()
        fetchData()
        setData()
    }

    private fun fetchData() {
        if (intent.hasExtra(MainActivity.INTEREST_PLACE_KEY)) {
            place = intent.extras!!.getParcelable(MainActivity.INTEREST_PLACE_KEY)
        }
    }

    override fun afterViews(savedInstanceState: Bundle?) {
        super.afterViews(savedInstanceState)
        ButterKnife.bind(this)
        setScreenFlags()
        fetchData()
        setData()
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

    private fun setData() {
        if (Utils.isEmpty(place!!.name)) {
            nameTextView!!.visibility = View.GONE
        } else {
            nameTextView!!.text = place!!.name
        }

        if (Utils.isEmpty(place!!.description)) {
            descriptionTextView!!.visibility = View.GONE
        } else {
            descriptionTextView!!.text = place!!.description
        }

        mailTextView!!.visibility = View.GONE

        if (!Utils.isEmpty(place!!.image)) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = 3000
            val animation = AnimationSet(true)
            animation.addAnimation(fadeIn)
            coverImage!!.animation = animation
            if (Utils.isEmpty(place!!.image)) {
                return
            }

            Picasso.with(this)
                    .load(place!!.image)
                    .resize(700, 700)
                    .centerCrop()
                    .into(coverImage)
        } else {
            coverImage!!.visibility = View.GONE
        }

        adapter = PlaceDetailViewPager()
        viewPager!!.adapter = adapter
    }

    override fun contentId(): Int {
        return R.layout.activity_place_details
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(INTEREST_PLACE_KEY, place)
    }

    @OnClick(R.id.fabButton)
    fun onFabButtonClick() {
        val intent = Intent(MapsActivity.getStartIntent(baseContext, place))
        startActivity(intent)
    }

    companion object {

        private val INTEREST_PLACE_KEY = "interestPlaceKey"

        fun getStartIntent(context: Context, place: PlaceOfInterest): Intent {
            val intent = Intent(context, PlaceDetailsActivity::class)
            intent.putExtra(MainActivity.INTEREST_PLACE_KEY, place)
            return intent
        }
    }
}
