package com.niedzwiecki.przemyslguide.ui.base;

import android.databinding.BindingAdapter;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Niedzwiecki on 11/11/2017.
 */

public class LoadingViewListBinder {

    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_INFO = 3;
    public static final int STATE_HIDDEN = 4;

    @BindingAdapter({"state"})
    public static void setState(LoadingView view, int state) {
        switch (state) {
            case STATE_LOADING:
                view.showProgressBar();
                break;
            case STATE_ERROR:
                view.showError();
                break;
            case STATE_INFO:
                view.showInfo();
                break;
            case STATE_HIDDEN:
                view.hide();
                break;
        }
    }

    @BindingAdapter({"state"})
    public static void footerStateBind(FooterView view, int state) {
        footerState(view, state);
    }

    public static void footerState(Footer view, int state) {
        if (view == null) {
            return;
        }

        switch (state) {
            case STATE_LOADING:
                view.showProgressBar();
                break;
            case STATE_ERROR:
                view.showError();
                break;
            case STATE_INFO:
                view.showInfo();
                break;
            case STATE_HIDDEN:
                view.hide();
                break;
        }
    }

    @BindingAdapter({"onErrorClick"})
    public static void setOnErrorClick(LoadingView loadingView, View.OnClickListener onClickListener) {
        loadingView.setOnErrorClick(onClickListener);
    }

    @BindingAdapter({"refreshing"})
    public static void setRefreshing(SwipeRefreshLayout swipeRefreshLayout, boolean refreshing) {
        swipeRefreshLayout.setRefreshing(refreshing);
    }

    @BindingAdapter({"enabledRefreshing"})
    public static void setRefreshingEnabled(SwipeRefreshLayout swipeRefreshLayout, boolean refreshingEnabled) {
        swipeRefreshLayout.setEnabled(refreshingEnabled);
    }

    @BindingAdapter({"columns"})
    public static void columns(RecyclerView recyclerView, int columns) {
        if (recyclerView.getLayoutManager() == null
                || !(recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }

        ((GridLayoutManager) recyclerView.getLayoutManager()).setSpanCount(columns);
    }

    @BindingAdapter({"adapter"})
    public static void adapter(RecyclerView recyclerView, RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

}
