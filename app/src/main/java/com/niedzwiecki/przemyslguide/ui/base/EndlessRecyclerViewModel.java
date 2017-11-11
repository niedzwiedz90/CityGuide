package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableList;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.niedzwiecki.przemyslguide.data.DataManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niedzwiecki on 11/11/2017.
 */

public class EndlessRecyclerViewModel<T extends
        EndlessRecyclerViewModel.DataManagerIntegration> extends BaseViewModel {


    public static final int COUNT_ITEMS_BEFORE_END_TO_LOAD_DATA = 3;
    public static final String FOOTER_TEXT_MESSAGE_KEY = "footerTextMessageKey";
    public static final String INFO_TEXT_KEY = "infoTextKey";
    public static final String LOADING_STATE_KEY = "loadingStateKey";
    public static final String COLUMNS_KEY = "columnsKey";
    public static final String REFRESHING_ENABLED_KEY = "refreshingEnabledKey";
    public static final String REFRESHING_KEY = "refreshingKey";
    public static final String FOOTER_STATE_KEY = "footerStateKey";
    public static final String NO_MORE_ITEMS_KEY = "noMoreItemsKey";
    public static final String IMAGE_KEY = "imageKey";
    public static final String TITLE_KEY = "titleKey";
    public static final String ACTION_TEXT_KEY = "actionTextKey";
    public static final String SHOW_FOOTER_VIEW_KEY = "showFooterViewKey";
    public static final String SCROLL_POSITION_KEY = "scrollPositionKey";
    public static final String SAVE_INSTANCE_ADDITIONAL_TEXT_KEY = "saveInstanceAdditionalTextKey";
    public static final String RECYCLER_VIEW_STATE_KEY = "SocialStreamFragment_recyclerVIew";
    public static final String SHOW_PROGRESSBAR_ON_ERROR_CLICKED = "showProgressbarOnErrorClicked";

    private final WeakReference<EndlessRecyclerViewModel> weakReference;

    private View.OnClickListener onErrorClickedListener;

    public ObservableList<T> items;

    public ObservableField<CharSequence> footerTextMessage;
    public ObservableField<CharSequence> infoText;
    public ObservableInt loadingState;
    public ObservableInt columns;
    public ObservableBoolean refreshingEnabled;
    public ObservableBoolean refreshing;
    public ObservableInt footerState;
    public ObservableBoolean noMoreItems;
    public ObservableInt image;
    public ObservableInt title;
    public ObservableField<String> actionText;
    public ObservableBoolean showProgressbarOnErrorClicked;

    public ObservableField<View.OnClickListener> onErrorClicked;
    public ObservableField<View.OnClickListener> onAdditionalErrorClicked;
    public int scrollPosition;
    private boolean showFooterView;

    public EndlessAdapter<T> adapter = new EndlessAdapter<T>(COUNT_ITEMS_BEFORE_END_TO_LOAD_DATA) {

        @Override
        protected void onStartLoad(int page) {
            EndlessRecyclerViewModel.this.loadData(true, page);
        }

        @Override
        public void onStopLoad() {

        }

        @Override
        public BaseItemView<T> createViewItem(Context context, int viewType) {
            if (viewType == FOOTER_VIEW_TYPE) {
                if (getFooterView() == null) {
                    FooterView<T> footerView = new FooterView<>(context);
                    footerView.setLayoutParams(new RecyclerView.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT)
                    );
                    footerView.setOnErrorClick(v -> loadData(true, adapter.getPage()));
                    setFooterView(footerView);
                    footerView.setInfoText(footerTextMessage.get());
                    LoadingViewListBinder.footerState(footerView, footerState.get());
                    footerView.requestLayout();
                    return footerView;
                } else {
                    ViewGroup parent = (ViewGroup) getFooterView().getView().getParent();
                    if (parent != null) {
                        parent.removeView(getFooterView().getView());
                    }

                    LoadingViewListBinder.footerState(getFooterView(), footerState.get());
                    getFooterView().setInfoText(footerTextMessage.get());
                    return getFooterView();
                }
            }

            return createItemView(context, viewType);
        }

        @Override
        public T getItem(int position) {
            return EndlessRecyclerViewModel.this.getItem(position);
        }

        @Override
        public int getItemViewType(int position) {
            return EndlessRecyclerViewModel.this.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return EndlessRecyclerViewModel.this.getItemCount();
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            onSetData(position, getItem(position));
        }
    };

    public EndlessRecyclerViewModel() {
        weakReference = new WeakReference<>(this);
        init();
    }

    private static void restoreAdapterItems(DataManager dataManager,
                                            @NonNull Bundle savedInstanceState,
                                            EndlessAdapter<? extends DataManagerIntegration> adapter) {
        List<? extends DataManagerIntegration> parcelableArrayList = savedInstanceState.getParcelableArrayList(SocialStreamFragment.ITEMS_KEY);
        for (DataManagerIntegration parcelable : parcelableArrayList) {
            parcelable.setDataManager(dataManager);
        }

        adapter.setItems(new ArrayList(parcelableArrayList));
    }

    private static void saveAdapterInstanceState(Bundle outState, EndlessAdapter<? extends Parcelable> adapter) {
        outState.putParcelableArrayList(SocialStreamFragment.ITEMS_KEY, new ArrayList<>(adapter.getItems()));
    }

    public static void saveRecyclerInstanceState(Bundle outState, RecyclerView recyclerView) {
        Parcelable value = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(
                RECYCLER_VIEW_STATE_KEY,
                value
        );
    }

    public void init() {
        showProgressbarOnErrorClicked = new ObservableBoolean(true);
        onRefreshListener = () -> {
            EndlessRecyclerViewModel endlessRecyclerViewModel = weakReference.get();
            if (endlessRecyclerViewModel != null) {
                endlessRecyclerViewModel.onRefresh();
            }
        };
        actionText = new ObservableField<>();
        showFooterView = true;
        refreshingEnabled = new ObservableBoolean(true);
        footerTextMessage = new ObservableField<>();
        footerTextMessage.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                adapter.setShowFooterView(showFooterView && footerState.get() != LoadingViewListBinder.STATE_HIDDEN);
                if (adapter.getFooterView() != null) {
                    adapter.getFooterView().setInfoText(footerTextMessage.get());
                }
            }
        });
        infoText = new ObservableField<>();
        loadingState = new ObservableInt();

        items = new ObservableArrayList<>();
        footerState = new ObservableInt();
        footerState.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int state) {
                adapter.setShowFooterView(showFooterView && footerState.get() != LoadingViewListBinder.STATE_HIDDEN);
                LoadingViewListBinder.footerState(adapter.getFooterView(), footerState.get());
            }
        });
        columns = new ObservableInt(1);
        noMoreItems = new ObservableBoolean(false);
        noMoreItems.addOnPropertyChangedCallback(new OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                adapter.setMoreItems(!noMoreItems.get());
                adapter.notifyDataSetChanged();
            }
        });

        image = new ObservableInt();
        title = new ObservableInt();
        refreshing = new ObservableBoolean(false);
        onErrorClickedListener = v -> {
            EndlessRecyclerViewModel endlessRecyclerViewModel = weakReference.get();
            if (endlessRecyclerViewModel != null) {
                endlessRecyclerViewModel.onRefresh();
            }
        };
        onErrorClicked = new ObservableField<>(onErrorClickedListener);
        onAdditionalErrorClicked = new ObservableField<>();
    }

    public T getItem(int position) {
        return adapter.isEmpty() ? null : adapter.isShowFooterView() && position == getItemCount() - 1 ? null : adapter.getItems().get(position);
    }

    public int getItemViewType(int position) {
        return adapter.isShowFooterView() && position == adapter.getItemCount() - 1 ?
                EndlessAdapter.FOOTER_VIEW_TYPE : 0;
    }

    public int getItemCount() {
        return adapter.isShowFooterView() ? adapter.getItems().size() + 1 : adapter.getItems().size();
    }

    public void onRefresh() {
        onRefresh(false);
    }

    public void onRefresh(boolean showProgress) {
        adapter.resetStates();
        loadData(showProgress, PageList.FIRST_PAGE);
    }

    public SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public static void beforeLoadData(EndlessRecyclerViewModel viewModel, int page, boolean showProgress) {
        if (showProgress) {
            if (page == 1) {
                viewModel.loadingState.set(LoadingViewListBinder.STATE_LOADING);
                viewModel.loadingState.notifyChange();

                viewModel.footerState.set(LoadingViewListBinder.STATE_HIDDEN);
                viewModel.footerState.notifyChange();
            } else {
                viewModel.footerState.set(LoadingViewListBinder.STATE_LOADING);
                viewModel.footerState.notifyChange();

                viewModel.loadingState.set(LoadingViewListBinder.STATE_HIDDEN);
                viewModel.loadingState.notifyChange();
            }
        }

    }

    public void onError(int page, UserNotLoggedInError throwable, int image, String message, String errorActionText) {
        // TODO: 20/02/2017 remove title, image from UserNotLoggedInError
        showProgressbarOnErrorClicked.set(false);
        throwable.setTitle(R.string.user_not_logged_in_title);
        throwable.setImage(image);
        throwable.setMessage(message);
        setUpActionUserNotLoggedIn();
        onErrorClicked.notifyChange();
        setUpAdditionalErrorClick();
        onAdditionalErrorClicked.notifyChange();
        actionText.set(errorActionText);
        onError(this, page, throwable.getMessage(), throwable.getTitle(), throwable.getImage());
    }

    private void setUpActionUserNotLoggedIn() {
        onErrorClicked.set(v -> {
            EndlessRecyclerViewModel endlessRecyclerViewModel = weakReference.get();
            if (endlessRecyclerViewModel != null) {
                if (endlessRecyclerViewModel.isNavigatorAttached()) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(LauncherActivity.LAUNCH_AS_GUEST_KEY, true);
                    bundle.putInt(LauncherActivity.STATE_ARG, LauncherActivity.STATE_REGISTER);
                    endlessRecyclerViewModel.getNavigator().startActivity(LauncherActivity.class, bundle);
                }
            }
        });
    }

    public void setUpAdditionalErrorClick() {
        showProgressbarOnErrorClicked.set(false);
        onAdditionalErrorClicked.set(v -> {
            EndlessRecyclerViewModel endlessRecyclerViewModel = weakReference.get();
            if (endlessRecyclerViewModel != null) {
                if (endlessRecyclerViewModel.isNavigatorAttached()) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(LauncherActivity.LAUNCH_AS_GUEST_KEY, true);
                    bundle.putInt(LauncherActivity.STATE_ARG, LauncherActivity.STATE_LOGIN);
                    endlessRecyclerViewModel.getNavigator().startActivity(LauncherActivity.class, bundle);
                }
            }
        });
    }

    public static void onError(EndlessRecyclerViewModel viewModel, String errorMessage, int page) {
        onError(viewModel, page, errorMessage, R.string.error, R.drawable.ic_error_default);
    }

    public void onError(String errorMessage, int page) {
        showProgressbarOnErrorClicked.set(true);
        onError(this, page, errorMessage, R.string.error, R.drawable.ic_error_default);
    }

    public static void onError(EndlessRecyclerViewModel viewModel, int page, String errorMessage, int errorTitle, int imageRes) {
        if (page == 1) {
            viewModel.image.set(imageRes);
            viewModel.image.notifyChange();
            viewModel.title.set(errorTitle);
            viewModel.title.notifyChange();
            viewModel.infoText.set(errorMessage);
            viewModel.infoText.notifyChange();
            viewModel.loadingState.set(LoadingViewListBinder.STATE_ERROR);
            viewModel.loadingState.notifyChange();

            viewModel.footerState.set(LoadingViewListBinder.STATE_HIDDEN);
            viewModel.footerState.notifyChange();

            viewModel.clearItems();
        } else {
            viewModel.loadingState.set(LoadingViewListBinder.STATE_HIDDEN);
            viewModel.loadingState.notifyChange();

            viewModel.footerTextMessage.set(errorMessage);
            viewModel.footerTextMessage.notifyChange();
            viewModel.footerState.set(LoadingViewListBinder.STATE_ERROR);
            viewModel.footerState.notifyChange();
        }

        viewModel.refreshing.set(false);
        viewModel.refreshing.notifyChange();
    }

    public static <T extends DataManagerIntegration> void handleResponse(EndlessRecyclerViewModel<T> viewModel, List<T> response, int page, boolean hasMoreItems, CharSequence emptyText, CharSequence endOfListText) {
        if (page == 1) {
            if (response == null || response.isEmpty()) {
                viewModel.setEmptyText(emptyText);
                viewModel.loadingState.set(LoadingViewListBinder.STATE_INFO);

                viewModel.footerState.set(LoadingViewListBinder.STATE_HIDDEN);
            } else {
                viewModel.loadingState.set(LoadingViewListBinder.STATE_HIDDEN);
                if (!hasMoreItems) {
                    viewModel.footerState.set(LoadingViewListBinder.STATE_INFO);
                    viewModel.footerTextMessage.set(endOfListText);
                }
            }

            viewModel.clearItems();
        } else {
            if (hasMoreItems) {
                viewModel.loadingState.set(LoadingViewListBinder.STATE_HIDDEN);
                viewModel.footerTextMessage.set(endOfListText);
                viewModel.footerState.set(LoadingViewListBinder.STATE_HIDDEN);
            } else {
                viewModel.loadingState.set(LoadingViewListBinder.STATE_HIDDEN);
                viewModel.footerTextMessage.set(endOfListText);
                viewModel.footerState.set(LoadingViewListBinder.STATE_INFO);
            }
        }

        viewModel.noMoreItems.set(!hasMoreItems);
        viewModel.addPage(response);

        viewModel.refreshing.set(false);
        viewModel.refreshing.notifyChange();
    }

    public void handleAddItem(EndlessRecyclerViewModel<T> viewModel, T item, int position, String endOfListText) {
        viewModel.loadingState.set(LoadingViewListBinder.STATE_HIDDEN);
        viewModel.footerState.set(LoadingViewListBinder.STATE_INFO);
        viewModel.footerTextMessage.set(endOfListText);

        viewModel.loadingState.notifyChange();
        viewModel.footerState.notifyChange();
        viewModel.footerTextMessage.notifyChange();

        adapter.addItem(item, position);
    }

    public void onSetData(int position, T item) {

    }

    @NonNull
    public BaseItemView<T> createItemView(Context context, int ViewType) {
        throw new RuntimeException("Override createItemView.");
    }

    public void startLoadingData() {
        loadData(true, PageList.FIRST_PAGE);
    }

    public void loadData(boolean showProgress, int page) {

    }

    protected void addPage(List<T> page) {
        adapter.addItems(page);
        items.addAll(page);
    }

    private void clearItems() {
        adapter.clearItems();
        items.clear();
    }

    private void setEmptyText(CharSequence emptyText) {
        infoText.set(emptyText);
    }

    @Bindable
    public EndlessAdapter getAdapter() {
        return adapter;
    }

    public void setShowFooterView(boolean showFooterView) {
        this.showFooterView = showFooterView;
        adapter.setShowFooterView(showFooterView);
    }

    @Override
    public void saveInstanceState(Bundle bundle) {
        super.saveInstanceState(bundle);
        bundle.putString(FOOTER_TEXT_MESSAGE_KEY, footerTextMessage.get() == null ? "" : footerTextMessage.get().toString());
        bundle.putString(INFO_TEXT_KEY, infoText.get() == null ? "" : infoText.get().toString());
        bundle.putString(ACTION_TEXT_KEY, actionText.get() == null ? "" : actionText.get());

        bundle.putInt(LOADING_STATE_KEY, loadingState.get());
        bundle.putInt(COLUMNS_KEY, columns.get());
        bundle.putInt(FOOTER_STATE_KEY, footerState.get());
        bundle.putInt(IMAGE_KEY, image.get());
        bundle.putInt(TITLE_KEY, title.get());
        bundle.putInt(SCROLL_POSITION_KEY, scrollPosition);

        bundle.putBoolean(REFRESHING_ENABLED_KEY, refreshingEnabled.get());
        bundle.putBoolean(REFRESHING_KEY, refreshing.get());
        bundle.putBoolean(NO_MORE_ITEMS_KEY, noMoreItems.get());
        bundle.putBoolean(SHOW_FOOTER_VIEW_KEY, showFooterView);
        bundle.putBoolean(SAVE_INSTANCE_ADDITIONAL_TEXT_KEY, onAdditionalErrorClicked != null && onAdditionalErrorClicked.get() != null);
        bundle.putBoolean(SHOW_PROGRESSBAR_ON_ERROR_CLICKED, showProgressbarOnErrorClicked.get());
        adapter.saveInstanceState(bundle);
        saveAdapterInstanceState(bundle, adapter);
    }

    @Override
    public void restoreInstanceState(Bundle bundle) {
        super.restoreInstanceState(bundle);
        showProgressbarOnErrorClicked.set(bundle.getBoolean(SHOW_PROGRESSBAR_ON_ERROR_CLICKED));
        footerTextMessage.set(bundle.getString(FOOTER_TEXT_MESSAGE_KEY));
        infoText.set(bundle.getString(INFO_TEXT_KEY));
        actionText.set(bundle.getString(ACTION_TEXT_KEY));

        loadingState.set(bundle.getInt(LOADING_STATE_KEY));
        columns.set(bundle.getInt(COLUMNS_KEY));
        footerState.set(bundle.getInt(FOOTER_STATE_KEY));
        image.set(bundle.getInt(IMAGE_KEY));
        title.set(bundle.getInt(TITLE_KEY));
        scrollPosition = bundle.getInt(SCROLL_POSITION_KEY);

        refreshingEnabled.set(bundle.getBoolean(REFRESHING_ENABLED_KEY));
        refreshing.set(bundle.getBoolean(REFRESHING_KEY));
        noMoreItems.set(bundle.getBoolean(NO_MORE_ITEMS_KEY));
        showFooterView = bundle.getBoolean(SHOW_FOOTER_VIEW_KEY);
        boolean shouldSetUpAdditionalText = bundle.getBoolean(SAVE_INSTANCE_ADDITIONAL_TEXT_KEY);
        if (shouldSetUpAdditionalText) {
            setUpAdditionalErrorClick();
            setUpActionUserNotLoggedIn();
        }

        adapter.restoreInstanceState(bundle);
        restoreAdapterItems(DataManager.getInstance(), bundle, adapter);
    }


    public interface DataManagerIntegration extends Parcelable {

        void setDataManager(DataManager dataManager);

    }
}