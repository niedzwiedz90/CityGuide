package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity;
import com.niedzwiecki.przemyslguide.util.Utils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.niedzwiecki.przemyslguide.ui.main.MainActivity.RIBOT_KEY;

/**
 * Created by niedzwiedz on 10.07.17.
 */

public class PlaceDetailsActivity extends BaseActivity {

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
        intent.putExtra(RIBOT_KEY, ribot);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
    }

    @Override
    public void afterViews() {
        super.afterViews();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        interestPlace = (InterestPlace) getIntent().getExtras().getSerializable(RIBOT_KEY);
        if (interestPlace != null) {
            FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fabButton);
            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PlaceDetailsActivity.this, "FAB CLICK", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.getStartIntent(getBaseContext(), interestPlace));
                    startActivityForResult(intent, 100);
                }
            });

            setData();
        }
    }

    @Override
    protected void afterViews(Bundle savedInstanceState) {
        super.afterViews(savedInstanceState);
        interestPlace = (InterestPlace) getIntent().getExtras().getSerializable(RIBOT_KEY);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        if (interestPlace != null) {
            FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fabButton);
            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PlaceDetailsActivity.this, "FAB CLICK", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MapsActivity.getStartIntent(getBaseContext(), interestPlace));
                    startActivityForResult(intent, 100);
                }
            });

            setData();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null) {
            afterViews(data.getBundleExtra(RIBOT_KEY));
        }
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
}
