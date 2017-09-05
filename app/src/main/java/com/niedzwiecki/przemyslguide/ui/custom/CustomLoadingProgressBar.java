package com.niedzwiecki.przemyslguide.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class CustomLoadingProgressBar extends ProgressBar {

    private static final int MIN_SHOW_TIME = 500;
    private final Runnable mDelayedShow;
    private boolean mPostedShow;
    private boolean mDismissed;

    public CustomLoadingProgressBar(Context context) {
        this(context, null);
    }

    public CustomLoadingProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.mPostedShow = false;
        this.mDismissed = false;
        this.mDelayedShow = new Runnable() {
            public void run() {
                CustomLoadingProgressBar.this.mPostedShow = false;
                if (!CustomLoadingProgressBar.this.mDismissed) {
                    CustomLoadingProgressBar.this.setVisibility(VISIBLE);
                }

            }
        };
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.removeCallbacks();
    }

    private void removeCallbacks() {
        this.removeCallbacks(this.mDelayedShow);
        this.mPostedShow = false;
    }

    @Override
    public void setVisibility(int v) {
        super.setVisibility(v);
    }

    public void hide() {
        this.mDismissed = true;
        this.removeCallbacks(this.mDelayedShow);
        this.setVisibility(GONE);
    }

    public void show() {
        this.mDismissed = false;
        if (!this.mPostedShow) {
            this.postDelayed(this.mDelayedShow, MIN_SHOW_TIME);
            this.mPostedShow = true;
        }

    }

}
