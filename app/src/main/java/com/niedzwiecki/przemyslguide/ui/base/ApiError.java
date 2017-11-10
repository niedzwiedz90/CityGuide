package com.niedzwiecki.przemyslguide.ui.base;

import android.support.annotation.NonNull;

import com.niedzwiecki.przemyslguide.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niedzwiecki on 11/10/2017.
 */

class ApiError extends RuntimeException {

    public String status;
    @NonNull
    private List<Cause> errors;
    private int code;

    public ApiError(String message, int code) {
        this.code = code;
        this.errors = new ArrayList<>();
        errors.add(new Cause(message));
    }

    public ApiError(String status, Cause cause) {
        this.status = status;
        this.errors = new ArrayList<>();
        this.errors.add(cause);
    }

    public ApiError(String status, Cause cause, Throwable errorCause) {
        super(errorCause);
        this.status = status;
        this.errors = new ArrayList<>();
        this.errors.add(cause);
    }

    public ApiError(String status, Cause cause, int code) {
        this.status = status;
        this.errors = new ArrayList<>();
        this.errors.add(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static class Cause {

        public String description;
        public String location;
        public String name;

        public Cause(String description) {
            this.description = description;
            this.location = "";
            this.name = "";
        }

    }

    @NonNull
    public List<Cause> getErrors() {
        return errors;
    }

    public boolean isEventOfflineError() {
        for (Cause cause : getErrors()) {
            if (cause.name.equals(Code.EVENT_OFFLINE_ERROR)) {
                return true;
            }
        }

        return false;
    }

 /*   public static ApiError fromRetrofitError(Retrofit retrofit, Throwable retrofitError) {
        if (retrofitError instanceof ApiError) {
            return (ApiError) retrofitError;
        }

        ApiError apiError = null;
        try {
            Converter<ResponseBody, ApiError> converter =
                    retrofit.responseBodyConverter(ApiError.class, new Annotation[0]);
            if (NetworkUtil.isHttpStatusCode(retrofitError, HttpURLConnection.HTTP_BAD_REQUEST)) {
                apiError = converter.convert(((HttpException) retrofitError).response().errorBody());
                apiError.code = HttpURLConnection.HTTP_BAD_REQUEST;
            } else if (NetworkUtil.isHttpStatusCode(retrofitError, HttpURLConnection.HTTP_GONE)) {
                apiError = converter.convert(((HttpException) retrofitError).response().errorBody());
                apiError.code = HttpURLConnection.HTTP_GONE;
            }

          *//*  if (NetworkUtil.isNoInternetException(retrofitError)) {
                NotificationController.postSafe(NotificationController.NETWORK_ERROR);
                apiError = getNetworkError();
            } else if (apiError == null) {
                apiError = new ApiError(ApplicationController.getInstance().getString(R.string.error), new Cause(ApplicationController.getInstance().getString(R.string.unexpected_error)), retrofitError);
                if (retrofitError instanceof HttpException) {
                    apiError.code = ((HttpException) retrofitError).code();
                }
            }
        } catch (Exception e) {
            apiError = new ApiError(ApplicationController.getInstance().getString(R.string.error), new Cause(ApplicationController.getInstance().getString(R.string.unexpected_error)), e);
        }*//*

        return apiError;
    }*/

  /*  @NonNull
    public static ApiError getNetworkError() {
        return new ApiError(ApplicationController.getInstance().getString(R.string.error),
                new Cause(ApplicationController.getInstance().getString(R.string.internet_connection_error)),
                NotificationController.NETWORK_ERROR);
    }*/

    @Override
    public String getMessage() {
        return getPrimaryCause();
    }

    public static ApiError fromGraphError() {
        return new ApiError(ApplicationController.getInstance().getString(R.string.error), new Cause(ApplicationController.getInstance().getString(R.string.unexpected_error)));
    }

    @Deprecated
    public String getPrimaryCause() {
        if (errors.size() > 0) {
            return errors.get(0).description;
        } else {
            return ApplicationController.getInstance().getString(R.string.Something_went_wrong);
        }
    }

    public boolean hasCause(@NonNull String name) {
        for (Cause error : errors) {
            if (name.equals(error.name)) {
                return true;
            }
        }

        return false;
    }

    public void setMessage(String string) {
        errors = new ArrayList<>();
        errors.add(0, new Cause(string));
    }

    public static class Code {

        public static final int ERROR_700_USER_ACCOUNT_REQUIRED = 700;
        public static final String EVENT_OFFLINE_ERROR = "event_offline";

    }

}
