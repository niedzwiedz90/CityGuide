package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DimenRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.Toast;

import com.niedzwiecki.przemyslguide.util.NetworkUtil;
import com.niedzwiecki.przemyslguide.util.Utils;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public class ResourcesManager {

    private final Context context;
    private static ResourcesManager instance;

    public ResourcesManager(Context context) {
        this.context = context;
    }

    public static ResourcesManager getInstance(Context context) {
        if (instance == null) {
            instance = new ResourcesManager(context);
        }

        return instance;
    }

    public String getString(int res) {
        return context.getString(res);
    }

    public String getString(int res, Object... formatArgs) {
        return context.getString(res, formatArgs);
    }

    public Drawable getDrawable(int res) {
        return ContextCompat.getDrawable(context, res);
    }

    public int getColor(int res) {
        return ContextCompat.getColor(context, res);
    }

    public void showToast(@NonNull String message, int duration) {
        Toast.makeText(context, message, duration).show();
    }

    public void showToastWithCenteredText(@NonNull String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, Utils.TOAST_OFFSET);
        toast.setText(message);
        toast.show();
    }

    public String[] getStringArray(int arrayId) {
        return context.getResources().getStringArray(arrayId);
    }

    public int getDensity() {
        return context.getResources().getDisplayMetrics().densityDpi;
    }

    public void showToast(@NonNull int res, int duration) {
        Toast.makeText(context, res, duration).show();
    }

    public float getDimension(@DimenRes int res) {
        return context.getResources().getDimension(res);
    }

    public int getDimensionInPixelSize(@DimenRes int res) {
        return context.getResources().getDimensionPixelSize(res);
    }

    public int getInteger(@IntegerRes int res) {
        return context.getResources().getInteger(res);
    }

    public void release() {
        instance = null;
    }

    public boolean isNetworkConnected() {
        return NetworkUtil.isNetworkConnected(context);
    }

}
