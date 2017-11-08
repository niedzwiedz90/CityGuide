package com.niedzwiecki.przemyslguide.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.view.MenuItem;
import android.view.View;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.SyncService;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.databinding.ActivityMainBinding;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.base.ViewModel;
import com.niedzwiecki.przemyslguide.ui.login.email.EmailActivity;
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity;
import com.niedzwiecki.przemyslguide.ui.placeDetails.PlaceDetailsActivity;
import com.niedzwiecki.przemyslguide.util.RecyclerItemClickListener;

import java.util.ArrayList;
import java.util.List;

import static com.niedzwiecki.przemyslguide.ui.login.password.PasswordActivity.EMAIL_KEY;
import static com.niedzwiecki.przemyslguide.ui.maps.MapsActivity.PLACES_LIST;

public class MainActivity extends BaseActivity {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.ribot.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    public static final String INTEREST_PLACE_KEY =
            "com.niedzwiecki.przemyslGuide.PlaceDetailActivity.key";

    PlacesAdapter placesAdapter;
    MainViewModel mainViewModel;

    private String email;
    private List<PlaceOfInterest> placesList;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntentRibot(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    public static void start(Activity context, String email) {
        Intent starter = new Intent(context, MainActivity.class);
        starter.putExtra(EMAIL_KEY, email);
        context.startActivity(starter);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void init() {
        placesAdapter = new PlacesAdapter();
        getViewDataBinding().recyclerView.setAdapter(placesAdapter);
        getViewDataBinding().recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        getViewDataBinding().recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, getViewDataBinding().recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        PlaceOfInterest places = placesAdapter.getPlace(position);
                        openDetail(places);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        getViewModel().loadPlaces();

        getViewDataBinding().navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                      /*  if (item.isChecked()) {
                            item.setChecked(false);
                        } else {
                            item.setChecked(true);
                        }*/

                        getViewDataBinding().drawerLayout.closeDrawers();

                        switch (item.getItemId()) {
                            case R.id.navMap:
                                filterPlaces("all");
                                return true;

                            case R.id.navMapWithHotels:
                                filterPlaces("hotel");
                                return true;
                            case R.id.navMapWithCastles:
                                filterPlaces("castle");
                                return true;
                            case R.id.navMapWithFort:
                                filterPlaces("station");
                                return true;
                            case R.id.navMapWithStation:
                                filterPlaces("station");
                                return true;
                            case R.id.navMapWithBunker:
                                filterPlaces("bunker");
                                return true;
                            case R.id.navLogout:
                                mainViewModel.logout();
                                return true;
                            default:
                                return true;
                        }
                    }
                });

        if (!getViewDataBinding().swipeToRefresh.isRefreshing()) {
            getViewDataBinding().swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    mainViewModel.loadPlaces();
                }
            });
        }
    }

    private void filterPlaces(String type) {
        if (placesList != null) {
            ArrayList<PlaceOfInterest> tempList = new ArrayList<>();
            for (PlaceOfInterest interestPlace : placesList) {
                if (interestPlace != null && interestPlace.type.equals(type)) {
                    tempList.add(interestPlace);
                } else if (type.equals("all")) {
                    tempList.add(interestPlace);
                }
            }

            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putExtra(PLACES_LIST, tempList);
            startActivity(intent);
        }
    }

    private void openDetail(PlaceOfInterest interestPlace) {
        startActivity(PlaceDetailsActivity.Companion.getStartIntent(this, interestPlace));
    }

    @Override
    public void beforeViews() {
        super.beforeViews();
        setDataBindingEnabled(true);
    }

    @Override
    public int contentId() {
        return R.layout.activity_main;
    }

    @Override
    public void afterViews() {
        super.afterViews();
        getViewDataBinding().setViewModel(getViewModel());

        if (getIntent().hasExtra(EMAIL_KEY)) {
            email = getIntent().getStringExtra(EMAIL_KEY);

//            View header = navigationView.getHeaderView(0);
//            TextView name = (TextView) header.findViewById(R.id.emailInfo);
//            name.setText(email);
        }

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }

        init();
    }

    @Override
    public ViewModel createViewModel() {
        return new MainViewModel(dataManager);
    }

    @Override
    public MainViewModel getViewModel() {
        return (MainViewModel) super.getViewModel();
    }

    @Override
    public ActivityMainBinding getViewDataBinding() {
        return (ActivityMainBinding) super.getViewDataBinding();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (viewModel != null) {
            viewModel.attachNavigator(this);
        }
    }

    @Override
    public void moveForward(Options options, Object... data) {
        super.moveForward(options, data);
        switch (options) {
            case SHOW_PLACES:
                if (getViewDataBinding().swipeToRefresh.isRefreshing()) {
                    getViewDataBinding().swipeToRefresh.setRefreshing(!getViewDataBinding().swipeToRefresh.isRefreshing());
                }

                placesList = (List<PlaceOfInterest>) data[0];
                showPlaces(placesList);

                break;
            case START_EMAIL_ACTIVITY:
                EmailActivity.start(this);
                break;
        }
    }

    //MVP
    public void showPlaces(List<PlaceOfInterest> interestPlaces) {
        placesAdapter.setPlaces(interestPlaces);
        placesAdapter.notifyDataSetChanged();
    }

}
