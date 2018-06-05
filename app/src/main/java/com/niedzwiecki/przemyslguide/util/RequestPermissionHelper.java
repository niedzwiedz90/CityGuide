package com.niedzwiecki.przemyslguide.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.niedzwiecki.przemyslguide.ui.base.ApplicationController;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * This is a helper class for handling run time permissions.
 *
 * To use this helper call checkPermissionsFromActivity() or checkPermissionsFromFragment() method. Then call onRequestPermissionsResult()
 * in Activity/Fragment  onRequestPermissionsResult() method. On call onShowPermissionRationale()
 * you should show dialog and call requestPermission() when user close it. Don't forget to call
 * unregisterCallback() in Activity/Fragment onDestroy().
 *
 * If you don't get any feedback in Fragment onRequestPermissionsResult() remember about
 * <a href="https://code.google.com/p/android/issues/detail?id=189121"> issue </a>
 * in support library.
 */
public class RequestPermissionHelper {

    private static RequestPermissionHelper instance = null;
    private static int requestCode;
    private static Map<Integer, RequestPermissionCallback> callbackMap;

    private RequestPermissionHelper() {
    }

    public static RequestPermissionHelper getInstance() {
        if (instance == null) {
            instance = new RequestPermissionHelper();
            callbackMap = new HashMap<>();
        }
        return instance;
    }

    public void checkPermissionsFromActivity(Activity activity, String permission, RequestPermissionCallback requestPermissionCallback){
        checkPermissionsFromActivity(activity, new String[] {permission}, requestPermissionCallback);
    }

    public void checkPermissionsFromActivity(Activity activity, String[] permissions, RequestPermissionCallback requestPermissionCallback) {
        int requestCode = addCallback(requestPermissionCallback);
        if (isPermissionGranted(permissions)) {
            requestPermissionCallback.onPermissionGranted(permissions);
        } else {
            requestPermission(activity, permissions, requestCode);
        }
    }

    public void checkPermissionsFromFragment(Fragment fragment, String permission, RequestPermissionCallback requestPermissionCallback){
        checkPermissionsFromFragment(fragment, new String[] {permission}, requestPermissionCallback);
    }

    public void checkPermissionsFromFragment(Fragment fragment, String[] permissions, RequestPermissionCallback requestPermissionCallback) {
        int requestCode = addCallback(requestPermissionCallback);
        if (isPermissionGranted(permissions)) {
            requestPermissionCallback.onPermissionGranted(permissions);
        } else {
            requestPermission(fragment, permissions, requestCode);
        }
    }

    public void requestPermission(Fragment fragment, String[] permissions, int requestCode) {
        fragment.requestPermissions(permissions, requestCode);
    }

    public void requestPermission(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    private boolean shouldShowRequestPermissionRationale(Activity activity, String[] permissions) {
        boolean showRationale = false;
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                showRationale = true;
            }
        }

        return showRationale;
    }

    public boolean isPermissionGranted(String permission){
        return isPermissionGranted(new String[] {permission});
    }

    public boolean isPermissionGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(ApplicationController.getInstance(), permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, Activity activity) {
        RequestPermissionCallback requestPermissionCallback = callbackMap.get(requestCode);
        if (requestPermissionCallback != null) {
            boolean permissionGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        permissionGranted = false;
                    }
                }
            }

            if (permissionGranted) {
                requestPermissionCallback.onPermissionGranted(permissions);
            } else {
                if (shouldShowRequestPermissionRationale(activity, permissions)) {
                    requestPermissionCallback.onShowPermissionRationale(activity, permissions, requestCode);
                } else {
                    requestPermissionCallback.onPermissionDenied(permissions);
                }
            }
        }
    }

    private int addCallback(RequestPermissionCallback requestPermissionCallback){
        for (Map.Entry<Integer, RequestPermissionCallback> entry : callbackMap.entrySet()) {
            if (entry.getValue().equals(requestPermissionCallback)) {
                return entry.getKey();
            }
        }
        callbackMap.put(++requestCode, requestPermissionCallback);
        return requestCode;
    }

    public void unregisterCallback(RequestPermissionCallback requestPermissionCallback) {
        if(requestPermissionCallback != null) {
            for (Iterator<Map.Entry<Integer, RequestPermissionCallback>> it = callbackMap.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<Integer, RequestPermissionCallback> entry = it.next();
                if (entry.getValue().equals(requestPermissionCallback)) {
                    it.remove();
                }
            }
        }
    }

    public interface RequestPermissionCallback {
        void onShowPermissionRationale(Activity activity, String[] permissions, int requestCode);

        void onPermissionDenied(String[] permissions);

        void onPermissionGranted(String[] permissions);
    }
}
