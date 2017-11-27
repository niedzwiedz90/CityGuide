package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public abstract class BaseRelativeView extends RelativeLayout implements BaseView {

    private ViewDataBinding viewDataBinding;
    protected boolean dataBidingEnabled;

    public BaseRelativeView(Context context) {
        super(context);
        init();
    }

    public BaseRelativeView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public BaseRelativeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        dataBidingEnabled = true;
        beforeViews();
        if (dataBidingEnabled) {
            viewDataBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(getContext()),
                    contentId(), this, true);
        } else {
            inflate(getContext(), contentId(), this);
        }

        afterViews();
    }


    public abstract int contentId();

    public void beforeViews() {

    }

    public void afterViews() {
    }

    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    @Override
    public View getView() {
        return this;
    }
}
