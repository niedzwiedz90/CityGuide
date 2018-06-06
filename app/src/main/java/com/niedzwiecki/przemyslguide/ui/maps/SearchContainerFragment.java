package com.niedzwiecki.przemyslguide.ui.maps;

public class SearchContainerFragment {

 /*   public static final int LISTING_FRAGMENT_TYPE = 10;
    public static final int MAP_FRAGMENT_TYPE = 20;
    public static final String KEY_FRAGMENT_TYPE = "keyFragmentType";
    SearchMapFragment searchMapFragment;
//    SearchImpFragment searchFragment;

    @Override
    public int getContentView() {
        return R.layout.fragments_container;
    }

    @Override
    public void afterViews(Context context) {
        super.afterViews(context);
    }

    @Override
    public void putBundle(Bundle bundle) {
        super.putBundle(bundle);
        if (getActivity() != null) {
            if (bundle != null && bundle.containsKey(KEY_FRAGMENT_TYPE)) {
                if (bundle.getInt(KEY_FRAGMENT_TYPE) == LISTING_FRAGMENT_TYPE) {
                    showListing();
                } else if (bundle.getInt(KEY_FRAGMENT_TYPE) == MAP_FRAGMENT_TYPE) {
                    showMap();
                } else {
                    throw new RuntimeException("Unsupported ALL_TYPE fragment.");
                }
            }
        }

    }

    public void showListing() {
        if (searchMapFragment.isVisible()) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.show(searchFragment);
            fragmentTransaction.hide(searchMapFragment);
            fragmentTransaction.commit();
        }
    }

    public void showMap() {
        if (searchFragment.isVisible()) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.hide(searchFragment);
            fragmentTransaction.show(searchMapFragment);
            fragmentTransaction.commit();
        }
    }

    public void setShouldRefreshData(boolean shouldRefresh) {
        searchFragment.setShouldRefreshList(shouldRefresh);
    }*/
}
