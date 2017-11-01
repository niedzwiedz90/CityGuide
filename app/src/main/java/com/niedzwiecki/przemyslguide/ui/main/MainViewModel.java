package com.niedzwiecki.przemyslguide.ui.main;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys;
import com.niedzwiecki.przemyslguide.data.model.Place;
import com.niedzwiecki.przemyslguide.data.model.Places;
import com.niedzwiecki.przemyslguide.data.model.PlacesResponse;
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

    public void loadPlaces() {
        RxUtil.unsubscribe(mSubscription);/*
        mSubscription = dataManager.getRibots()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
               .subscribe(new Subscriber<PlacesResponse>() {
                   @Override
                   public void onCompleted() {

                   }

                   @Override
                   public void onError(Throwable e) {
                       Timber.e(e ,"<---ERROR RESPONSE");
                   }

                   @Override
                   public void onNext(PlacesResponse placesResponse) {
                        Timber.d("PLACES RESPONSE --->", placesResponse);
                       getNavigator().moveForward(Navigator.Options.SHOW_PLACES, placesResponse);
                   }
               });*/

        mSubscription = dataManager.getRibots()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<Place>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                Timber.e(throwable ,"<---ERROR RESPONSE");
            }

            @Override
            public void onNext(List<Place> places) {
                Timber.d("PLACES RESPONSE --->", places.get(0));
            }
        });
    }

    public void logout() {
        dataManager.getPreferencesHelper().clearAuthenticationHeader(PreferencesKeys.LOGION_HEADER);
        getNavigator().moveForward(Navigator.Options.START_EMAIL_ACTIVITY);
        getNavigator().finish();
    }

}
