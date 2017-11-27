package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.databinding.ViewLoadingBinding;
import com.niedzwiecki.przemyslguide.util.Utils;

import java.lang.ref.WeakReference;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Niedzwiecki on 11/11/2017.
 */

public class LoadingView extends BaseRelativeView {

    private OnClickListener onErrorClick;
    private boolean showProgressBarOnErrorClick;
    private DataManager dataManager;
    CompositeSubscription compositeSubscription;

    public LoadingView(Context context) {
        super(context);
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init() {
        dataManager = ApplicationController.getInstance().getDataModule().provideDataManager();
        WeakReference<LoadingView> weakReference = new WeakReference<>(this);
        if (isInEditMode()) {
            return;
        }

        showProgressBarOnErrorClick = true;
//        showProgressBar();
//        setUpOnClick(weakReference);
    }

    @Override
    public int contentId() {
        return R.layout.view_loading;
    }

    @Override
    public ViewLoadingBinding getViewDataBinding() {
        return (ViewLoadingBinding) super.getViewDataBinding();
    }

    /*public void setUpOnClick(WeakReference<LoadingView> weakReference) {
        getViewDataBinding().alertButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingView loadingView = weakReference.get();
                if (loadingView == null) {
                    return;
                }

                if (loadingView.showProgressBarOnErrorClick) {
                    loadingView.showProgressBar();
                }

                if (loadingView.onErrorClick != null) {
                    loadingView.onErrorClick.onClick(v);
                }
            }
        });
    }
*/
    public void setShowProgressBarOnErrorClick(boolean showProgressBarOnErrorClick) {
        this.showProgressBarOnErrorClick = showProgressBarOnErrorClick;
    }

    public void showError() {
        getViewDataBinding().alertText.setVisibility(View.VISIBLE);
        getViewDataBinding().alertButton.setVisibility(VISIBLE);
        getViewDataBinding().progressBar.setVisibility(View.GONE);
        show();
    }

    public void showError(ApiError error) {
        getViewDataBinding().alertText.setVisibility(View.VISIBLE);
        getViewDataBinding().alertText.setText(error.getPrimaryCause());
        getViewDataBinding().alertButton.setVisibility(View.VISIBLE);
        getViewDataBinding().progressBar.setVisibility(View.GONE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
        show();
    }

    public void showError(String message) {
        getViewDataBinding().alertText.setVisibility(View.VISIBLE);
        getViewDataBinding().alertText.setText(message);
        getViewDataBinding().alertButton.setVisibility(View.VISIBLE);
        getViewDataBinding().progressBar.setVisibility(View.GONE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
        show();
    }

    public void showError(ApiError error, OnClickListener onClickListener) {
        getViewDataBinding().alertText.setVisibility(View.VISIBLE);
        getViewDataBinding().alertText.setText(error.getPrimaryCause());
        getViewDataBinding().alertButton.setVisibility(View.VISIBLE);
        setOnErrorClick(onClickListener);
        getViewDataBinding().progressBar.setVisibility(View.GONE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
        show();
    }

    public void showError(String message, OnClickListener onClickListener) {
        getViewDataBinding().alertText.setVisibility(View.VISIBLE);
        getViewDataBinding().alertText.setText(message);
        getViewDataBinding().alertButton.setVisibility(View.VISIBLE);
        setOnErrorClick(onClickListener);
        getViewDataBinding().progressBar.setVisibility(View.GONE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
        show();
    }

    public void showError(String message, String buttonText, OnClickListener onClickListener) {
        setActionText(buttonText);
        showError(message, onClickListener);
    }


    public void setActionText(CharSequence buttonText) {
        if (Utils.isEmpty(buttonText)) {
            return;
        }

        getViewDataBinding().alertButton.setText(buttonText);
    }

    public void showInfo(CharSequence message) {
        getViewDataBinding().alertText.setVisibility(View.VISIBLE);
        getViewDataBinding().alertText.setText(message);
        getViewDataBinding().alertButton.setVisibility(View.GONE);
        getViewDataBinding().progressBar.setVisibility(View.GONE);
        getViewDataBinding().imageView.setVisibility(GONE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
        getViewDataBinding().title.setVisibility(GONE);
        show();
    }

    public void showInfo() {
        getViewDataBinding().alertText.setVisibility(View.VISIBLE);
        getViewDataBinding().alertButton.setVisibility(View.GONE);
        getViewDataBinding().progressBar.setVisibility(View.GONE);
        getViewDataBinding().imageView.setVisibility(GONE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
        getViewDataBinding().title.setVisibility(GONE);
        show();
    }

    public void showProgressBar() {
        getViewDataBinding().title.setVisibility(GONE);
        getViewDataBinding().imageView.setVisibility(GONE);
        getViewDataBinding().alertText.setVisibility(View.GONE);
        getViewDataBinding().alertButton.setVisibility(View.GONE);
        getViewDataBinding().progressBar.setVisibility(View.VISIBLE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
//        hideProgressBarIfTestMode();
        show();
    }
/*
    private void hideProgressBarIfTestMode() {
        if (ApplicationController.getInstance().isMockMode()) {
            new Handler().postDelayed(() -> setVisibility(GONE), 100);
        }
    }*/

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE && getAlpha() > 0.9f;
    }

    public void setOnErrorClick(OnClickListener onClickListener) {
        this.onErrorClick = onClickListener;
    }

    public void setOnAdditionalErrorClick(OnClickListener onClickListener) {
        this.getViewDataBinding().additionalAction.setOnClickListener(onClickListener);
        this.getViewDataBinding().additionalAction.setVisibility(onClickListener == null ? GONE : VISIBLE);
    }

    public void setInfoText(CharSequence emptyMessage) {
        getViewDataBinding().alertText.setText(emptyMessage);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
/*
        Utils.unsubscribe(compositeSubscription);
        compositeSubscription = new CompositeSubscription();
        compositeSubscription.add(dataManager.subscribeEvent(BusEvent.Event.NETWORK_CONNECTED)
                .doOnNext(event -> performAlertButtonClick())
                .subscribe());
        compositeSubscription.add(dataManager.subscribeEvent(BusEvent.Event.REFRESH_DATA)
                .doOnNext(event -> performAlertButtonClick())
                .subscribe());*/
    }

    private void performAlertButtonClick() {
        if (getViewDataBinding().alertButton.getVisibility() == VISIBLE) {
            getViewDataBinding().alertButton.performClick();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        Utils.unsubscribe(compositeSubscription);
        super.onDetachedFromWindow();
    }

    public void setImage(int res) {
        getViewDataBinding().imageView.setImageResource(res);
        getViewDataBinding().imageView.setVisibility(VISIBLE);
    }

    public void setTitle(int res) {
        if (res != 0) {
            getViewDataBinding().title.setText(res);
            getViewDataBinding().title.setVisibility(VISIBLE);
        }
    }

    public String getInfoText() {
        return getViewDataBinding().alertText.getText().toString();
    }

    public void setProgressColorFilter(int color) {
        getViewDataBinding().progressBar.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }
}
