package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.os.Bundle;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;

import static com.niedzwiecki.przemyslguide.ui.main.MainActivity.RIBOT_KEY;

/**
 * Created by niedzwiedz on 10.07.17.
 */

public class PlaceDetailsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_place_details);
        Bundle ribot = getIntent().getBundleExtra(RIBOT_KEY);
        
    }

    @Override
    public int contentId() {
        return 0;
    }
}
