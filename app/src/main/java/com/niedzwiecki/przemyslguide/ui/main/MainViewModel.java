package com.niedzwiecki.przemyslguide.ui.main;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys;
import com.niedzwiecki.przemyslguide.data.model.PlaceOfInterest;
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel;
import com.niedzwiecki.przemyslguide.ui.base.Navigator;
import com.niedzwiecki.przemyslguide.util.RxUtil;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends BaseViewModel {

    private final DataManager dataManager;
    private Subscription mSubscription;

    public MainViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    public void loadPlaces() {
        RxUtil.unsubscribe(mSubscription);
        mSubscription = dataManager.getPlaces()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<PlaceOfInterest>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable ,"<---ERROR RESPONSE");
                loadPlaces();
            }

            @Override
            public void onNext(List<PlaceOfInterest> places) {
                Timber.d("PLACES RESPONSE --->", places);
                getNavigator().moveForward(Navigator.Options.SHOW_PLACES, places);
            }
        });
    }

    public void logout() {
        dataManager.getPreferencesHelper().clearAuthenticationHeader(PreferencesKeys.LOGION_HEADER);
        getNavigator().moveForward(Navigator.Options.START_EMAIL_ACTIVITY);
        getNavigator().finish();
    }

}
