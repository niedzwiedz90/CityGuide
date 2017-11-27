package com.niedzwiecki.przemyslguide.ui.main;

import android.databinding.BaseObservable;
import android.os.Parcel;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.model.InterestPlace;
import com.niedzwiecki.przemyslguide.ui.base.EndlessRecyclerViewModel;

/**
 * Created by Niedzwiecki on 11/16/2017.
 */

public class MainRowViewModel extends BaseObservable implements EndlessRecyclerViewModel.DataManagerIntegration {

    private DataManager dataManager;
    private InterestPlace item;

    public MainRowViewModel(DataManager dataManager, InterestPlace item) {
        this.dataManager = dataManager;
        this.item = item;
    }

    public String getPlaceName() {
        return item.getName();
    }

    public String getImageUrl() {
        return item.getImage();
    }

    @Override
    public void setDataManager(DataManager dataManager) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
