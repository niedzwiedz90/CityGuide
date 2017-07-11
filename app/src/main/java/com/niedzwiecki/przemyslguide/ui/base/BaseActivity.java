package com.niedzwiecki.przemyslguide.ui.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.niedzwiecki.przemyslguide.BoilerplateApplication;
import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.injection.component.ActivityComponent;
import com.niedzwiecki.przemyslguide.injection.component.ConfigPersistentComponent;
import com.niedzwiecki.przemyslguide.injection.component.DaggerConfigPersistentComponent;
import com.niedzwiecki.przemyslguide.injection.module.ActivityModule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import butterknife.BindView;
import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        Navigator,
        ViewModelIntegration {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();
    private static final String VERSION_PREFS_UPDATE_NEEDED = "UpdateNeeded";
    private static final String VERSION_PREFS_LAST_VERSION_TO_UPDATE = "LastVersionToUpdate";

    private ActivityComponent mActivityComponent;
    private long mActivityId;
    private ViewDataBinding viewDataBinding;
    private boolean dataBindingEnabled;
    public ViewModel viewModel;
/*
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();
        ConfigPersistentComponent configPersistentComponent;
        if (!sComponentsMap.containsKey(mActivityId)) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(BoilerplateApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        } else {
            Timber.i("Reusing ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = sComponentsMap.get(mActivityId);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));

        beforeViews();
        if (contentId() != 0 && !dataBindingEnabled) {
            setContentView(contentId());
        } else if (contentId() != 0 && dataBindingEnabled) {
//            viewDataBinding = DataBindingUtil.setContentView(this, contentId());
        } else {
            Timber.d("You didn't setup content layout at: %s", getClass().getName());
        }

        if (dataBindingEnabled) {
            viewModel = viewModel != null ? viewModel : createViewModel();
//            toolbar = (Toolbar) findViewById(R.id.toolbar);
        }

        if (savedInstanceState == null) {
            afterViews();
        } else {
            if (getViewModel() != null) {
                getViewModel().restoreInstanceState(savedInstanceState);
            }

            afterViews(savedInstanceState);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }
        super.onDestroy();
    }

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }

    @Override
    public ViewModel createViewModel() {
        return null;
    }

    @Override
    public void moveForward(Options options, Object... data) {

    }

    @Override
    public void goBack() {

    }

    @Override
    public void showProgress(@NonNull String title) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showError(@NonNull String title) {

    }

    @Override
    public void showError(@NonNull String title, @NonNull String content) {

    }

    @Override
    public void showInfo(@NonNull String title, @NonNull String content, @NonNull String positiveButtonText, DialogInterface.OnClickListener positiveClick) {

    }

    @Override
    public void showInfo(@NonNull String title, @NonNull String content, @NonNull String positiveButtonText, DialogInterface.OnClickListener positiveClick, @NonNull String negativeButtonText, DialogInterface.OnClickListener negativeClick) {

    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, Serializable serializable) {

    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, String key, Serializable serializable) {

    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {

    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, Serializable serializable, int requestCode) {

    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, String key, Serializable serializable, int requestCode) {

    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, Bundle bundle, int requestCode) {

    }

    protected void afterViews(Bundle savedInstanceState) {

    }

    public void beforeViews() {

    }

    public void afterViews() {

    }

    public abstract int contentId();

    public void setDataBindingEnabled(boolean dataBindingEnabled) {
        this.dataBindingEnabled = dataBindingEnabled;
    }

    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    private void setupToolbarWithDataBinding(Toolbar toolbar, boolean showHomeAsUp) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() == null) {
                return;
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            if (showHomeAsUp) {
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.common_google_signin_btn_icon_light);
            }
        } else {
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel != null) {
            viewModel.attachNavigator(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewModel != null) {
            viewModel.detachNavigator();
        }
    }

    /*private void clearPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(VERSION_PREFS_LAST_VERSION_TO_UPDATE);
        editor.remove(VERSION_PREFS_UPDATE_NEEDED);
        editor.commit();
    }
*/

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

}
