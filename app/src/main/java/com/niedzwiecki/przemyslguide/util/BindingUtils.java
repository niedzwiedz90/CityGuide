package com.niedzwiecki.przemyslguide.util;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

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

}

