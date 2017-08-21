package com.niedzwiecki.przemyslguide.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.niedzwiecki.przemyslguide.ui.login.password.ErrorModel;

import java.io.IOException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

public class NetworkUtil {

    /**
     * Returns true if the Throwable is an instance of RetrofitError with an
     * http status code equals to the given one.
     */
    public static boolean isHttpStatusCode(Throwable throwable, int statusCode) {
        return throwable instanceof HttpException
                && ((HttpException) throwable).code() == statusCode;
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static boolean isNoInternetException(Throwable e) {
        return e instanceof UnknownHostException;
    }

    public static ErrorModel getErrorModel(Throwable e) {
        String errorJson = null;
        try {
            errorJson = ((HttpException)e).response().errorBody().string();
        } catch (IOException e1) {
            ErrorModel errorModel = new ErrorModel();
            errorModel.detail = e.getMessage();
            return errorModel;
        }

        return new Gson().fromJson(errorJson, ErrorModel.class);
    }

}