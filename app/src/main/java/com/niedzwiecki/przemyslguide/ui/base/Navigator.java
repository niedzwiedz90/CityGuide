package com.niedzwiecki.przemyslguide.ui.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

/**
 * Created by niedzwiedz on 29.06.17.
 */

public interface Navigator {

    void moveForward(Options options, Object... data);
    void finish();
    void goBack();

    void showProgress(@NonNull String title);
    void hideProgress();

    void showError(@NonNull String title);
    void showError(@NonNull String title, @NonNull String content);

    void showInfo(@NonNull String title, @NonNull String content,
                  @NonNull String positiveButtonText,
                  DialogInterface.OnClickListener positiveClick);

    void showInfo(@NonNull String title, @NonNull String content,
                  @NonNull String positiveButtonText,
                  DialogInterface.OnClickListener positiveClick,
                  @NonNull String negativeButtonText,
                  DialogInterface.OnClickListener negativeClick
    );

    void startActivity(Class<? extends Activity> activityClass);
    void startActivity(Class<? extends Activity> activityClass, Serializable serializable);
    void startActivity(Class<? extends Activity> activityClass, String key, Serializable serializable);
    void startActivity(Class<? extends Activity> activityClass, Bundle bundle);

    void startActivityForResult(Class<? extends Activity> activityClass, int requestCode);
    void startActivityForResult(Class<? extends Activity> activityClass, Serializable serializable, int requestCode);
    void startActivityForResult(Class<? extends Activity> activityClass, String key, Serializable serializable, int requestCode);
    void startActivityForResult(Class<? extends Activity> activityClass, Bundle bundle, int requestCode);

    enum Options {
        SHOW_PLACES,
        SHOW_KEYBOARD,
        START_PASSWORD_ACTIVITY,
        START_EMAIL_ACTIVITY,
        START_MAIN_ACTIVITY,
        START_ACTIVITY_WITH_INTENT,
        SHOW_FILTERED_PLACES
    }

}
