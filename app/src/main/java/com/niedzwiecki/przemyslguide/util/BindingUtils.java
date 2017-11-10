package com.niedzwiecki.przemyslguide.util;

import android.databinding.BindingAdapter;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import java.util.Locale;

import timber.log.Timber;

/**
 * Created by Niedzwiecki on 11/10/2017.
 */

public class BindingUtils {

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String url) {
        ViewUtils.loadImage(view, false, url);
    }

    @BindingAdapter({"imageUrlFit"})
    public static void loadImageFit(ImageView view, String url) {
        ViewUtils.loadImage(view, false, url, true);
    }

    @BindingAdapter({"imageCircleUrl"})
    public static void loadCircleImage(ImageView view, String url) {
        ViewUtils.loadImage(view, true, url);
    }

    @BindingAdapter({"imageCircleUrlWithDefault", "imageDefault"})
    public static void loadCircleImageWithGuestDefault(ImageView view, String url, int resDefault) {
        ViewUtils.loadImage(view, true, url, resDefault);
    }

    @BindingAdapter({"imageCircleUrlCropped"})
    public static void loadCircleImageCropped(ImageView view, @Nullable String url) {
        if (Utils.isEmpty(url)) {
            ViewUtils.loadImage(view, true, url);
        } else {
            try {
                String key = Utils.encode("ev3nto4y", url);
                key = key.replace("\n", "");
                int measuredWidth = view.getMeasuredWidth();
                String formattedUrl = String.format(Locale.US, "https://m.eventory.cc/api/imageproxy/%d,s%s/%s", measuredWidth, key, url);
                ViewUtils.loadImage(view, true, formattedUrl);
            } catch (Exception e) {
                Timber.e(e, "Tried get image key.");
                ViewUtils.loadImage(view, true, url);
            }

        }
    }
}

