package com.niedzwiecki.przemyslguide.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.databinding.ViewLoadingBinding;
import com.niedzwiecki.przemyslguide.ui.base.ApplicationController;
import com.niedzwiecki.przemyslguide.util.Utils;

import java.lang.ref.WeakReference;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Niedzwiecki on 2018-06-05.
 */
public class LoadingView extends BaseRelativeView {

    private View.OnClickListener onErrorClick;
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
        setUpOnClick(weakReference);
    }

    @Override
    protected int contentId() {
        return R.layout.view_loading;
    }

    @Override
    public ViewLoadingBinding getViewDataBinding() {
        return (ViewLoadingBinding) super.getViewDataBinding();
    }

    public void setUpOnClick(final WeakReference<LoadingView> weakReference) {
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

    public void showProgressBar() {
        getViewDataBinding().title.setVisibility(GONE);
        getViewDataBinding().imageView.setVisibility(GONE);
        getViewDataBinding().alertText.setVisibility(View.GONE);
        getViewDataBinding().alertButton.setVisibility(View.GONE);
        getViewDataBinding().progressBar.setVisibility(View.VISIBLE);
        getViewDataBinding().additionalAction.setVisibility(GONE);
        show();
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public boolean isVisible() {
        return getVisibility() == View.VISIBLE && getAlpha() > 0.9f;
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

}
