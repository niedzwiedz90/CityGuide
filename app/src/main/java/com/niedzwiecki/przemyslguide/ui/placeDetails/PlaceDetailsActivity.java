package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.ui.PlaceDetailViewPager;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.main.MainActivity;
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity;
import com.niedzwiecki.przemyslguide.util.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by niedzwiedz on 10.07.17.
 */

public class PlaceDetailsActivity extends BaseActivity {

    private static final String INTEREST_PLACE_KEY = "interestPlaceKey";

    @BindView(R.id.nameOfRibot)
    TextView nameTextView;

    @BindView(R.id.description)
    TextView descriptionTextView;

    @BindView(R.id.coverImage)
    AppCompatImageView coverImage;

    @BindView(R.id.mailTextView)
    TextView mailTextView;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private PlaceOfInterest place;
    private PlaceDetailViewPager adapter;

    public static Intent getStartIntent(Context context, PlaceOfInterest place) {
        Intent intent = new Intent(context, PlaceDetailsActivity.class);
        intent.putExtra(MainActivity.INTEREST_PLACE_KEY, place);
        return intent;
    }

    @Override
    public void beforeViews() {
        super.beforeViews();
        activityComponent().inject(this);
    }

    @Override
    public void afterViews() {
        super.afterViews();
        ButterKnife.bind(this);
        setScreenFlags();
        fetchData();
        setData();
    }

    private void fetchData() {
        if (getIntent().hasExtra(MainActivity.INTEREST_PLACE_KEY)) {
            place = getIntent().getExtras().getParcelable(MainActivity.INTEREST_PLACE_KEY);
        }
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        super.afterViews(savedInstanceState);
        ButterKnife.bind(this);
        setScreenFlags();
        fetchData();
        setData();
        if (savedInstanceState != null) {
            restoreData(savedInstanceState);
        }
    }

    private void restoreData(Bundle savedInstanceState) {
        place = savedInstanceState.getParcelable(INTEREST_PLACE_KEY);
    }

    private void setScreenFlags() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setData() {
        if (Utils.isEmpty(place.name)) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setText(place.name);
        }

        if (Utils.isEmpty(place.description)) {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setText(place.description);
        }

        mailTextView.setVisibility(View.GONE);

        if (!Utils.isEmpty(place.image)) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(3000);
            AnimationSet animation = new AnimationSet(true);
            animation.addAnimation(fadeIn);
            coverImage.setAnimation(animation);
            if (Utils.isEmpty(place.image)) {
                return;
            }

            Picasso.with(this)
                    .load(place.image)
                    .resize(700, 700)
                    .centerCrop()
                    .into(coverImage);
        } else {
            coverImage.setVisibility(View.GONE);
        }

        adapter = new PlaceDetailViewPager();
        viewPager.setAdapter(adapter);
    }

    @Override
    public int contentId() {
        return R.layout.activity_place_details;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(INTEREST_PLACE_KEY, place);
    }

    @OnClick(R.id.fabButton)
    public void onFabButtonClick() {
        Intent intent = new Intent(MapsActivity.getStartIntent(getBaseContext(), place));
        startActivity(intent);
    }
}
