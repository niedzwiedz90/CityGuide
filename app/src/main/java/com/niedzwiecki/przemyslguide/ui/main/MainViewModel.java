package com.niedzwiecki.przemyslguide.ui.main;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel;
import com.niedzwiecki.przemyslguide.ui.base.Navigator;
import com.niedzwiecki.przemyslguide.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends BaseViewModel {

    private final DataManager dataManager;
    private Subscription mSubscription;

    @Inject
    public MainViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void loadRibots() {
//        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = dataManager.getRibots()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Ribot>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e(e, "There was an error loading the ribots.");
//                        getNavigator().showError();
                    }

                    @Override
                    public void onNext(List<Ribot> ribots) {
                        if (!ribots.isEmpty()) {
                            getNavigator().moveForward(Navigator.Options.SHOW_RIBOTS, ribots);
                            Timber.d("SHOWING RIBOTS !!!");
                        } else {
                            Timber.d("NOT SHOWING");
                        }
                    }
                });
    }

    public void logout() {
        dataManager.getPreferencesHelper().clearAuthenticationHeader(PreferencesKeys.LOGION_HEADER);
        getNavigator().moveForward(Navigator.Options.START_EMAIL_ACTIVITY);
        getNavigator().finish();
    }

}
