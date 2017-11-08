package com.niedzwiecki.przemyslguide.ui.placeDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest
import com.niedzwiecki.przemyslguide.databinding.ActivityPlaceDetailsBinding
import com.niedzwiecki.przemyslguide.ui.PlaceDetailViewPager
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity
import com.niedzwiecki.przemyslguide.ui.base.ViewModel
import com.niedzwiecki.przemyslguide.ui.main.MainActivity
import com.niedzwiecki.przemyslguide.util.Utils
import com.squareup.picasso.Picasso

/**
 * Created by niedzwiedz on 10.07.17.
 */

class PlaceDetailsActivity : BaseActivity() {


/*    //    @BindView(R.id.nameOfRibot)
    private var nameTextView: TextView? = null

    //    @BindView(R.id.description)
    internal var descriptionTextView: TextView? = null

    //    @BindView(R.id.coverImage)
    internal var coverImage: AppCompatImageView? = null

    //    @BindView(R.id.mailTextView)
    internal var mailTextView: TextView? = null

    //    @BindView(R.id.viewPager)
    internal var viewPager: ViewPager? = null*/

    private var place: PlaceOfInterest? = null
    private var adapter: PlaceDetailViewPager? = null

    override fun beforeViews() {
        super.beforeViews()
        activityComponent().inject(this)
    }

    override fun afterViews() {
        super.afterViews()
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
            viewDataBinding.nameOfRibot.visibility = View.GONE
        } else {
            viewDataBinding.nameOfRibot!!.text = place!!.name
        }

        if (Utils.isEmpty(place!!.description)) {
            viewDataBinding.description.visibility = View.GONE
        } else {
            viewDataBinding.description!!.text = place!!.description
        }

        viewDataBinding.mailTextView!!.visibility = View.GONE

        if (!Utils.isEmpty(place!!.image)) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.duration = 3000
            val animation = AnimationSet(true)
            animation.addAnimation(fadeIn)
            viewDataBinding.coverImage!!.animation = animation
            if (Utils.isEmpty(place!!.image)) {
                return
            }

            Picasso.with(this)
                    .load(place!!.image)
                    .resize(700, 700)
                    .centerCrop()
                    .into(viewDataBinding.coverImage)
        } else {
            viewDataBinding.coverImage!!.visibility = View.GONE
        }

        adapter = PlaceDetailViewPager()
        viewDataBinding.viewPager!!.adapter = adapter
    }

    override fun contentId(): Int {
        return R.layout.activity_place_details
    }

    override fun getViewDataBinding(): ActivityPlaceDetailsBinding {
        return super.getViewDataBinding() as ActivityPlaceDetailsBinding
    }

    override fun createViewModel(): ViewModel {
        return super.createViewModel()
    }

    /*  override fun createViewModel(): ViewModel {
          return PlacesDetailsViewModel()
      }

      override fun getViewModel(): PlacesDetailsViewModel {
          return super.getViewModel() as PlacesDetailsViewModel
      }
  */
    override fun setDataBindingEnabled(dataBindingEnabled: Boolean) {
        super.setDataBindingEnabled(true)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(INTEREST_PLACE_KEY, place)
    }
/*
    //    @OnClick(R.id.fabButton)
    fun onFabButtonClick() {
        val intent = Intent(MapsActivity.getStartIntent(baseContext, place))
        startActivity(intent)
    }*/

    companion object {

        private val INTEREST_PLACE_KEY = "interestPlaceKey"

        fun getStartIntent(context: Context, place: PlaceOfInterest): Intent {
            val intent = Intent(context, PlaceDetailsActivity::class.java)
            intent.putExtra(MainActivity.INTEREST_PLACE_KEY, place)
            return intent
        }
    }
}
