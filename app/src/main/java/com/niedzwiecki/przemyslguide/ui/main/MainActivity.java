package com.niedzwiecki.przemyslguide.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.SyncService;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.databinding.ActivityMainBinding;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.base.ViewModel;
import com.niedzwiecki.przemyslguide.ui.placeDetails.PlaceDetailsActivity;
import com.niedzwiecki.przemyslguide.util.DialogFactory;
import com.niedzwiecki.przemyslguide.util.RecyclerItemClickListener;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.ribot.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

    public static final String RIBOT_KEY =
            "com.niedzwiecki.przemyslGuide.PlaceDetailActivity.key";

    @Inject
    RibotsAdapter mRibotsAdapter;

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Inject
    MainViewModel mainViewModel;

    ActivityMainBinding binding;

    OnRibotClicked onRibotClicked;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

     /*   binding =
                DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(mainViewModel);
*/
        mRecyclerView.setAdapter(mRibotsAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, mRecyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Ribot ribot = mRibotsAdapter.getRibot(position);
                        openDetail(ribot);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                }));

        mainViewModel.attachNavigator(this);
        mainViewModel.loadRibots();

    }

    private void openDetail(Ribot ribot) {
        startActivity(PlaceDetailsActivity.getStartIntent(this, ribot));
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
        setViewModel(createViewModel());

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    public ViewModel createViewModel() {
        return mainViewModel;
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
            case SHOW_RIBOTS:
                List<Ribot> ribots = (List<Ribot>) data[0];
                showRibots(ribots);
        }
    }

    public void showRibots(List<Ribot> ribots) {
        mRibotsAdapter.setRibots(ribots);
        mRibotsAdapter.notifyDataSetChanged();
    }

    public void showError() {
        DialogFactory.createGenericErrorDialog(this, getString(R.string.error_loading_ribots))
                .show();
    }

    public void showRibotsEmpty() {
        mRibotsAdapter.setRibots(Collections.<Ribot>emptyList());
        mRibotsAdapter.notifyDataSetChanged();
        Toast.makeText(this, R.string.empty_ribots, Toast.LENGTH_LONG).show();
    }

    public interface OnRibotClicked {
        void sendRibot(Ribot ribot);
    }

}
