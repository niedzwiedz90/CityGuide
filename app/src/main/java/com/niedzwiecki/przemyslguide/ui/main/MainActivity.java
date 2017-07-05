package com.niedzwiecki.przemyslguide.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.SyncService;
import com.niedzwiecki.przemyslguide.databinding.ActivityMainBinding;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.base.ViewModel;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    private static final String EXTRA_TRIGGER_SYNC_FLAG =
            "uk.co.ribot.androidboilerplate.ui.main.MainActivity.EXTRA_TRIGGER_SYNC_FLAG";

/*    @Inject
    MainViewModel mMainPresenter;
    @Inject RibotsAdapter mRibotsAdapter;*/

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    /**
     * Return an Intent to start this Activity.
     * triggerDataSyncOnCreate allows disabling the background sync service onCreate. Should
     * only be set to false during testing.
     */
    public static Intent getStartIntent(Context context, boolean triggerDataSyncOnCreate) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_TRIGGER_SYNC_FLAG, triggerDataSyncOnCreate);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activityComponent().inject(this);
        ButterKnife.bind(this);
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
    protected void afterViews(Bundle savedInstanceState) {
        super.afterViews(savedInstanceState);
    }

    @Override
    public void afterViews() {
        super.afterViews();
/*        mRecyclerView.setAdapter(mRibotsAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));*/
//        mMainPresenter.attachView(this);
//        mMainPresenter.loadRibots();
        setViewModel(createViewModel());

        if (getIntent().getBooleanExtra(EXTRA_TRIGGER_SYNC_FLAG, true)) {
            startService(SyncService.getStartIntent(this));
        }
    }

    @Override
    public ViewModel createViewModel() {
        return new MainViewModel();
    }

    @Override
    public MainViewModel getViewModel() {
        return (MainViewModel) super.getViewModel();

    }

    @Override
    public ActivityMainBinding getViewDataBinding() {
        return (ActivityMainBinding) super.getViewDataBinding();

    }
}
