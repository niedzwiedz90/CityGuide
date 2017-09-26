package com.niedzwiecki.przemyslguide.ui.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

public class BaseRelativeView extends RelativeLayout implements BaseView {
    public BaseRelativeView(Context context) {
        super(context);
        init();
    }

    public BaseRelativeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseRelativeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaseRelativeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        beforeViews();
        inflate(getContext(), contentId(), this);
        ButterKnife.bind(this);
        afterViews();
    }

    public void beforeViews() {

    }

    @Override
    public int contentId() {
        return 0;
    }

    @Override
    public void afterViews() {

    }

}
