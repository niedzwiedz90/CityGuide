package com.niedzwiecki.przemyslguide.ui.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.ButterKnife;


public abstract class FrameBaseView extends FrameLayout {
    protected Menu menu;

    public FrameBaseView(Context context) {
        super(context);
        init();
    }

    public FrameBaseView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public FrameBaseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        beforeViews();
        inflate(getContext(), contentId(), this);
        ButterKnife.bind(this);
        afterViews();
    }

    protected abstract int contentId();

    public void beforeViews() {

    }

    protected void afterViews() {
    }

    public void saveInstanceState(Bundle savedInstanceState) {
    }

    public boolean onMenuItemClick(MenuItem menuItem) {
        return false;
    }

    public void onSaveInstanceState(Bundle outState) {
    }

    public void onRestoreInstanceState(Bundle state) {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return false;
    }


    public boolean onBackPressed() {
        return false;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void onStop() {
    }
}
