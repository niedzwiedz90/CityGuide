package com.niedzwiecki.przemyslguide.util;

import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.niedzwiecki.przemyslguide.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by Niedzwiecki on 11/10/2017.
 */

public class ViewUtils {

    public static void loadImage(ImageView imageView, boolean circle, String url) {
        loadImage(imageView, circle, url, R.drawable.photo_camera, -1, null, false);
    }

    public static void loadImage(ImageView imageView, boolean circle, String url, @DrawableRes int defaultPhoto) {
        loadImage(imageView, circle, url, defaultPhoto, -1, null, false);
    }

    public static void loadImage(ImageView imageView, boolean circle, String url, @DrawableRes int defaultPhoto, @DrawableRes int errorPhoto, @Nullable final Callback callback, boolean fitCenterInside) {
        RequestCreator load = Picasso.with(imageView.getContext()).load(url);

        if (errorPhoto != -1) {
            load.error(errorPhoto);
        }

        if (circle) {
            load.transform(Utils.getPicassoCircularTransformations());
        }

        if (defaultPhoto != -1 && defaultPhoto != 0) {
            load.placeholder(defaultPhoto);
        }

        if (fitCenterInside) {
            load.fit().centerInside();
        }

        if (callback == null) {
            load.into(imageView);
        } else {
            load.into(imageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    callback.onSuccess();
                }

                @Override
                public void onError() {
                    callback.onError();
                }
            });
        }
    }

    public static void loadImage(ImageView image, boolean circle, String url, boolean fitCenterInside) {
        loadImage(image, circle, url, R.drawable.photo_camera, -1, null, fitCenterInside);
    }

    public interface Callback {

        void onSuccess();

        void onError();

    }
}
