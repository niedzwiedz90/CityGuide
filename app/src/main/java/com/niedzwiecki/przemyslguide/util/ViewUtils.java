package com.niedzwiecki.przemyslguide.util;

import android.graphics.Bitmap;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Selection;
import android.text.SpannableString;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.ui.base.ApplicationController;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import timber.log.Timber;

/**
 * Created by Niedzwiecki on 11/10/2017.
 */

public class ViewUtils {


    public static void loadImage(Target target, boolean circle, String url) {
        RequestCreator load = Picasso.with(ApplicationController.getInstance()).load(url);
        if (circle) {
            load.transform(Utils.getPicassoCircularTransformations());
        }

        load.into(target);
    }

    public static void loadImage(ImageView imageView, boolean circle, String url, Callback callback) {
        loadImage(imageView, circle, url, R.drawable.ic_launcher_background, -1, callback, false);
    }

    public static void loadImage(ImageView imageView, boolean coverPhoto, boolean circle, String url) {
        loadImage(imageView, circle, url, R.drawable.ic_launcher_background, -1, null, false);
    }

    public static void loadImage(ImageView imageView, boolean circle, String url) {
        loadImage(imageView, circle, url, R.drawable.ic_launcher_background, -1, null, false);
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
        loadImage(image, circle, url, R.drawable.ic_launcher_background, -1, null, fitCenterInside);
    }

    public static RotateAnimation getInfiniteRotationAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(
                0.0f,
                360.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
        );
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setDuration(500);
        return rotateAnimation;
    }

    public static void removeOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
        }
    }

    public static void setClippedPadding(RecyclerView recyclerView) {
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), (int) recyclerView.getContext().getResources().getDimension(R.dimen.clipped_padding));
    }

    public static void attacheOnGlobalLayoutListener(View view, ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener) {
        removeOnGlobalLayoutListener(view, onGlobalLayoutListener);
        view.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public interface Callback {

        void onSuccess();

        void onError();

    }

    public static abstract class Target implements com.squareup.picasso.Target {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            onBitmapLoaded(bitmap);
        }

        public abstract void onBitmapLoaded(Bitmap bitmap);
    }

    public static class TouchTextView implements View.OnTouchListener {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if (!(v instanceof TextView)) {
                return false;
            }

            TextView textView = (TextView) v;
            SpannableString spannable = new SpannableString(textView.getText());

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= textView.getTotalPaddingLeft();
                y -= textView.getTotalPaddingTop();

                x += textView.getScrollX();
                y += textView.getScrollY();

                Layout layout = textView.getLayout();
                if (layout == null) {
                    return false;
                }

                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                try {
                    ClickableSpan[] link = spannable.getSpans(off, off, ClickableSpan.class);
                    if (link == null) {
                        return false;
                    }

                    if (link.length != 0) {
                        if (action == MotionEvent.ACTION_UP) {
                            if (link[0] != null) {
                                link[0].onClick(textView);
                            }
                        } else {
                            if (link[0] != null) {
                                Selection.setSelection(spannable,
                                        spannable.getSpanStart(link[0]),
                                        spannable.getSpanEnd(link[0]));
                            }
                        }

                        return true;
                    } else {
                        Selection.removeSelection(spannable);
                    }
                } catch (Exception e) {
                    Timber.e(e, "Exception touch link.");
                }
            }

            return false;
        }
    }
}
