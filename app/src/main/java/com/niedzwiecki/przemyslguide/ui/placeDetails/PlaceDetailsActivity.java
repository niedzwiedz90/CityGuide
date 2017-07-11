package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;

import butterknife.BindView;

import static com.niedzwiecki.przemyslguide.ui.main.MainActivity.RIBOT_KEY;

/**
 * Created by niedzwiedz on 10.07.17.
 */

public class PlaceDetailsActivity extends BaseActivity {

    @BindView(R.id.textOfRibot)
    TextView textView;


    private Ribot ribot;

    public static Intent getStartIntentRibot(Context context, Ribot event) {
        Intent intent = new Intent(context, PlaceDetailsActivity.class);
        intent.putExtra(RIBOT_KEY, event);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_place_details);

        if(getIntent().hasExtra(RIBOT_KEY)) {
            ribot = (Ribot) getIntent().getSerializableExtra(RIBOT_KEY);

            /*textView.setText(String.format("%s %s",
                    ribot.profile().name().first(), ribot.profile().name().last()));*/
        }

    }

    @Override
    public int contentId() {
        return 0;
    }
}
