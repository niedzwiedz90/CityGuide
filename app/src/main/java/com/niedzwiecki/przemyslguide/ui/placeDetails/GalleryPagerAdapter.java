package com.niedzwiecki.przemyslguide.ui.placeDetails;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.niedzwiecki.przemyslguide.ui.gallery.ScreenSlidePageFragment;

/**
 * Created by niedzwiedz on 03.09.17.
 */

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 5;

    public GalleryPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return new ScreenSlidePageFragment();
    }

}
