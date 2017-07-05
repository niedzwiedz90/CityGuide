package com.niedzwiecki.przemyslguide.ui.main;

import android.view.View;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel;
import com.niedzwiecki.przemyslguide.ui.maps.MapsActivity;
import com.niedzwiecki.przemyslguide.util.RxUtil;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class MainViewModel extends BaseViewModel {

    private final DataManager mDataManager;
    private Subscription mSubscription;

    @Inject
    public MainViewModel() {
        mDataManager = null;
    }

    public void loadRibots() {
//        checkViewAttached();
        RxUtil.unsubscribe(mSubscription);
        mSubscription = mDataManager.getRibots()
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
                        if (ribots.isEmpty()) {
//                            getMvpView().showRibotsEmpty();
                        } else {
//                            getMvpView().showRibots(ribots);
                        }
                    }
                });
    }

    public void onMapClick(@SuppressWarnings("unused") View view) {
        getNavigator().startActivity(MapsActivity.class);
        Timber.d("ONCLICK  NA MAPBUTTON");
    }

}
