package com.niedzwiecki.przemyslguide.ui.main;

import java.util.List;

import com.niedzwiecki.przemyslguide.data.model.Ribot;
import com.niedzwiecki.przemyslguide.ui.base.ViewModel;

public interface MainMvpView extends ViewModel {

    void showRibots(List<Ribot> ribots);

    void showRibotsEmpty();

    void showError();

}
