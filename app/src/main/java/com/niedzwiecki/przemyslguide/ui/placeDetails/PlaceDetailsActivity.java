package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.TextView;
import android.widget.Toast;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
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

    private Ribot ribot;

    public static Intent getStartIntent(Context context, Ribot ribot) {
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

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);

        ribot = getIntent().getExtras().getParcelable(RIBOT_KEY);
        if (ribot != null) {
            FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fabButton);
            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PlaceDetailsActivity.this, "FAB CLICK", Toast.LENGTH_SHORT).show();
                }
            });

            setData();
        }
    }

    private void setData() {
        if (Utils.isEmpty(ribot.profile().name().first())
                || Utils.isEmpty(ribot.profile().name().last())) {
            nameTextView.setVisibility(View.GONE);
        } else {
            nameTextView.setText(String.format("%s %s",
                    ribot.profile().name().first(), ribot.profile().name().last()));
        }

        if (Utils.isEmpty(ribot.profile().bio())) {
            descriptionTextView.setVisibility(View.GONE);
        } else {
            descriptionTextView.setText(ribot.profile().bio());
        }

        if (Utils.isEmpty(ribot.profile().email())) {
            mailTextView.setVisibility(View.GONE);
        } else {
            mailTextView.setText(ribot.profile().email());
        }

        if (!Utils.isEmpty(ribot.profile().avatar())) {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setDuration(3000);
            AnimationSet animation = new AnimationSet(true);
            animation.addAnimation(fadeIn);
            coverImage.setAnimation(animation);
            coverImage.setBackgroundColor(Color.parseColor(ribot.profile().hexColor()));
            Picasso.with(this)
                    .load(ribot.profile().avatar())
                    .resize(700, 700)
                    .centerCrop()
                    .into(coverImage);
        }
    }

    @Override
    public int contentId() {
        return R.layout.activity_place_details;
    }
}
