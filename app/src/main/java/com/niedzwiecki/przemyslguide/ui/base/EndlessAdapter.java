package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Niedzwiecki on 11/8/2017.
 */

public class EndlessAdapter<T> extends BaseRecyclerAdapter<T> {

    public static final int FOOTER_VIEW_TYPE = -2;
    public static final String ITEMS_BEAFORE_END_KEY = "itemsBeaforeEndKey";
    public static final String LOADING_DATA_KEY = "loadingDataKey";
    public static final String PREVIOUS_COUNT_KEY = "previousCountKey";
    public static final String MORE_ITEMS_KEY = "moreItemsKey";
    public static final String PAGE_KEY = "pageKey";
    public static final String IS_REFRESHING_DATA_KEY = "isRefreshingDataKey";
    public static final String SHOW_FOOTER_VIEW_KEY = "showFooterViewKey";
    protected int itemsBeforeEnd;
    protected boolean loadingData = true;
    protected int previousCount = -1;
    protected boolean moreItems = true;
    private WeakReference<Footer<T>> footerView;
    int page = 1;
    private boolean isRefreshingData;
    private boolean showFooterView;

    public EndlessAdapter() {
        this(3);
    }

    public EndlessAdapter(int countItemsBeforeEndToLoadData) {
        this.itemsBeforeEnd = countItemsBeforeEndToLoadData;
        loadingData = true;
    }

    public Footer<T> getFooterView() {
        if (footerView == null) {
            return null;

        }

        return footerView.get();
    }

    public void setLoadingData(boolean loadingData) {
        this.loadingData = loadingData;
    }

    public void setFooterView(Footer<T> footerView) {
        this.footerView = new WeakReference<>(footerView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        checkState(position);
    }

    @Override
    public void addItems(List<T> items) {
        page++;
        super.addItems(items);
    }

    protected void checkState(int i) {
        if (getItemCount() - i <= itemsBeforeEnd &&
                !loadingData && moreItems && !isRefreshingData) {
            loadingData = true;
            previousCount = super.getItemCount();
            onStartLoad(page);
        }

        if ((loadingData && previousCount < super.getItemCount())) {
            loadingData = false;
            isRefreshingData = false;
            onStopLoad();
            previousCount = super.getItemCount();
        }
    }

    protected void onStartLoad(int page) {

    }

    public void onStopLoad() {

    }

    public boolean isLoadingData() {
        return loadingData;
    }

    public void resetStates() {
        previousCount = -1;
        loadingData = false;
        page = 1;
        isRefreshingData = false;
    }

    public void startRefreshingData() {
        previousCount = -1;
        page = 1;
        isRefreshingData = true;
        setMoreItems(true);
        clearItems();
        onStartLoad(page);
    }

    public void stopRefreshingData() {
        this.isRefreshingData = false;
    }

    public boolean isRefreshing() {
        return isRefreshingData;
    }

    public boolean isMoreItems() {
        return moreItems;
    }

    public void setMoreItems(boolean moreItems) {
        this.moreItems = moreItems;
    }

    public void setNoMoreItems() {
        moreItems = false;
    }

    @Override
    public int getItemCount() {
        return showFooterView ? super.getItemCount() + 1 : super.getItemCount();
    }
/*
    public void setShowFooterView(boolean showFooterView) {
        this.showFooterView = showFooterView;
        new Handler().post(this::notifyDataSetChanged);
    }*/

    @Override
    public BaseItemView<T> createViewItem(Context context, int viewType) {
        if (viewType == FOOTER_VIEW_TYPE && getFooterView() != null) {
            if (getFooterView().getView().getParent() != null) {
                ((ViewGroup) getFooterView().getView().getParent()).removeView(getFooterView().getView());
            }

            return getFooterView();
        } else {
            return null;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return showFooterView && position + 1 == getItemCount() ?
                FOOTER_VIEW_TYPE : super.getItemViewType(position);
    }

    @Override
    public T getItem(int position) {
        return showFooterView && position >= getItemCount() ? null : super.getItem(position);
    }

    public int getPage() {
        return page;
    }

    public void loadData(boolean b, int page) {
        onStartLoad(page);
    }

    public boolean isShowFooterView() {
        return showFooterView;
    }

    public void saveInstanceState(Bundle bundle) {
        bundle.putInt(ITEMS_BEAFORE_END_KEY, this.itemsBeforeEnd);
        bundle.putBoolean(LOADING_DATA_KEY, this.loadingData);
        bundle.putInt(PREVIOUS_COUNT_KEY, previousCount);
        bundle.putBoolean(MORE_ITEMS_KEY, moreItems);
        bundle.putInt(PAGE_KEY, page);
        bundle.putBoolean(IS_REFRESHING_DATA_KEY, isRefreshingData);
        bundle.putBoolean(SHOW_FOOTER_VIEW_KEY, showFooterView);
    }

    public void restoreInstanceState(Bundle bundle) {
        this.itemsBeforeEnd = bundle.getInt(ITEMS_BEAFORE_END_KEY);
        loadingData = bundle.getBoolean(LOADING_DATA_KEY, this.loadingData);
        previousCount = bundle.getInt(PREVIOUS_COUNT_KEY, previousCount);
        moreItems = bundle.getBoolean(MORE_ITEMS_KEY, moreItems);
        page = bundle.getInt(PAGE_KEY, page);
        isRefreshingData = bundle.getBoolean(IS_REFRESHING_DATA_KEY, isRefreshingData);
        showFooterView = bundle.getBoolean(SHOW_FOOTER_VIEW_KEY, showFooterView);
    }
}
