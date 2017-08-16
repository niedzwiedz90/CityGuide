package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity;

import butterknife.BindView;

import static com.niedzwiecki.przemyslguide.ui.main.MainActivity.RIBOT_KEY;

/**
 * Created by niedzwiedz on 10.07.17.
 */

public class PlaceDetailsActivity extends BaseActivity {

    @BindView(R.id.textOfRibot2)
    TextView textView;

    private Ribot ribot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_place_details);
        getSupportActionBar().hide();

        if (getIntent().hasExtra(RIBOT_KEY)) {
            ribot = (Ribot) getIntent().getSerializableExtra(RIBOT_KEY);
            FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fabButton);
            fabButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(PlaceDetailsActivity.this, "FAB CLICK", Toast.LENGTH_SHORT).show();
                    startActivity(MapsActivity.class);
                }
            });

            if (ribot != null) {
                textView.setText(String.format("%s %s",
                        ribot.profile().name().first(), ribot.profile().name().last()));
            }

        }

    }

    @Override
    public int contentId() {
        return 0;
    }
}
