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
import android.widget.Toast;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.model.InterestPlace;
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

    private InterestPlace interestPlace;
    private PlaceDetailViewPager adapter;

    public static Intent getStartIntent(Context context, InterestPlace ribot) {
        Intent intent = new Intent(context, PlaceDetailsActivity.class);
        intent.putExtra(MainActivity.INTEREST_PLACE_KEY, ribot);
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
            interestPlace = (InterestPlace) getIntent().getExtras().getSerializable(MainActivity.INTEREST_PLACE_KEY);
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
        interestPlace = (InterestPlace) savedInstanceState.getSerializable(INTEREST_PLACE_KEY);
    }

    private void setScreenFlags() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void setData() {
        if (Utils.isEmpty(interestPlace.name)) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setText(interestPlace.name);
        }

        if (Utils.isEmpty(interestPlace.address)) {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setText(interestPlace.address);
        }

        mailTextView.setVisibility(View.GONE);

        if (!Utils.isEmpty(interestPlace.image)) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(3000);
            AnimationSet animation = new AnimationSet(true);
            animation.addAnimation(fadeIn);
            coverImage.setAnimation(animation);
            Picasso.with(this)
                    .load(interestPlace.image)
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
        outState.putSerializable(INTEREST_PLACE_KEY, interestPlace);
    }

    @OnClick(R.id.fabButton)
    public void onFabButtonClick() {
        Intent intent = new Intent(MapsActivity.getStartIntent(getBaseContext(), interestPlace));
        startActivity(intent);
    }
}
