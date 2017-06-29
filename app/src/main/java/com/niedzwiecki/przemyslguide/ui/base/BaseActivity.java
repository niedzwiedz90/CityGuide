package com.niedzwiecki.przemyslguide.ui.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.niedzwiecki.przemyslguide.BoilerplateApplication;
import com.niedzwiecki.przemyslguide.injection.component.ActivityComponent;
import com.niedzwiecki.przemyslguide.injection.component.ConfigPersistentComponent;
import com.niedzwiecki.przemyslguide.injection.component.DaggerConfigPersistentComponent;
import com.niedzwiecki.przemyslguide.injection.module.ActivityModule;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import timber.log.Timber;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity implements Navigator, ViewModelIntegration {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final Map<Long, ConfigPersistentComponent> sComponentsMap = new HashMap<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;

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
    public ViewModel getViewModel() {
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

    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, Serializable serializable) {

    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, String key, Serializable serializable) {

    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, Bundle bundle) {

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
}
