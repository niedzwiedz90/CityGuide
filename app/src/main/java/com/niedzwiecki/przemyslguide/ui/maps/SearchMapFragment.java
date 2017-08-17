package com.niedzwiecki.przemyslguide.ui.maps;

public class SearchMapFragment/* extends BaseFragment
        implements OnMapReadyCallback, SearchMapMvpView, GoogleMap.OnCameraChangeListener*/ {

    /*@Inject SearchMapPresenter presenter;

    @Bind(R.id.action_view) ActionView actionView;
    @Bind(R.id.container)
    FrameLayout containerFrameLayout;
    @Bind(R.id.map)
    FrameLayout mapContainer;

    Handler handler = new Handler(Looper.getMainLooper());
    private GoogleMap mMap;
    private Runnable runnable;
    private MarkerManager.Collection collection;
    private BaseRecycleAdapter<EstateModel> adapter;
    private BottomSheetDialog dialog;
    private TouchableWrapper touchView;
    private TextView zoomLabel;
    private TextView boundsLabel;
    private boolean loadClusters;
    private boolean debug;
    private MapFragmentWrapper mapFragment;
    public TouchableWrapper touchableWrapper;

    @Override
    public int getContentView() {
        return R.layout.fragment_search_map;
    }

    @Override
    public void afterViews(Context context) {
        super.afterViews(context);
        if (mapFragment == null) {
            mapFragment = new MapFragmentWrapper();
            FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
            transaction.replace(R.id.map, mapFragment);
            transaction.commit();
        }

        if (presenter == null) {
            getFragmentComponent().inject(this);
        }
        presenter.attachView(this);

        mapFragment.getMapAsync(this);
        MapsInitializer.initialize(getContext());
        actionView.setOnErrorButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onErrorClicked();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }

        if (collection != null) {
            collection.clear();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        MarkerManager manager = new MarkerManager(mMap);
        if (collection != null) {
            collection.clear();
        }
        collection = manager.newCollection();
        presenter.onMapReady(isHidden());
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                presenter.onMarkerClicked(marker, (int) mMap.getCameraPosition().zoom);
                return true;
            }
        });
        presenter.init();
        mMap.setOnCameraChangeListener(this);
    }

    @Override
    public void zoomToMarker(Marker marker) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(),
                mMap.getCameraPosition().zoom));
    }

    @Override
    public void showBottomListItems(Marker marker, int zoom) {
        dialog = new BottomSheetDialog(getContext(),
                R.style.AppBottomSheetDialogTheme);

        dialog.setContentView(R.layout.bottom_sheet_dialog);
        final RecyclerView recycler = (RecyclerView) dialog.findViewById(R.id.recycler_alert);
        if (recycler == null) {
            return;
        }

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter = new BaseRecycleAdapter<EstateModel>() {
            @Override
            public BaseItemView<EstateModel> createViewItem(Context context, int viewType) {
                EstateMapRow estateMapRow = new EstateMapRow(context);
                estateMapRow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = recycler.getChildAdapterPosition(v);
                        presenter.onBottomSheetItemClicked(position);
                    }
                });
                return estateMapRow;
            }
        });

        presenter.loadListEstates(Long.parseLong(marker.getSnippet()));
    }

    *//*****
     * MVP View methods implementation
     *****//*

    @Override
    public void updateLabels() {
        if (zoomLabel == null || boundsLabel == null || debug || !BuildConfig.DEBUG) {
            return;
        }

        zoomLabel.setText(String.format(Locale.getDefault(),
                "Zoom: %f", presenter.dataManager.getZoom()));
        boundsLabel.setText(String.format(Locale.getDefault(),
                "Bounds: [%f * %f * %f * %f]",
                presenter.dataManager.getLonMin(),
                presenter.dataManager.getLatMin(),
                presenter.dataManager.getLonMax(),
                presenter.dataManager.getLatMax()));
    }

    @Override
    public void moveCamera(double centerLon, double centerLat, float zoom) {
        Timber.d("UPDATES move camera zoom %s", zoom);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(centerLat, centerLon),
                zoom));
    }

    @Override
    public void setLoadClusters(boolean loadClusters) {
        this.loadClusters = loadClusters;
    }

    public int getMapWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    public int getMapHeight() {
        int height = getResources().getDisplayMetrics().heightPixels;
        int toolbarHeight = 48;
        int bottomBarHeight = 48;
        return height - dpToPx(toolbarHeight + bottomBarHeight);
    }

    @Override
    public void moveCamera(double latMin, double lonMin, double latMax, double lonMax) {
//        if (isHidden()) {
//            return;
//        }

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int toolbarHeight = 48;
        int bottomBarHeight = 48;

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(
                new LatLngBounds(
                        new LatLng(latMin, lonMin),
                        new LatLng(latMax, lonMax)),
                width,
                height - dpToPx(toolbarHeight + bottomBarHeight),
                dpToPx(10)), new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {
                Timber.d("Camera finish moving");
                enableCameraListener(true);
            }

            @Override
            public void onCancel() {

            }
        });
    }

    boolean enableCameraMove;

    @Override
    public void enableCameraListener(boolean enable) {
        this.enableCameraMove = enable;
    }

    private Bitmap singleMarker(int price) {
        IconGenerator iconGenerator = new IconGenerator(getContext());
        iconGenerator.setTextAppearance(getContext(), R.style.BaseTextAccent);
        iconGenerator.setContentPadding(dpToPx(2), dpToPx(0), dpToPx(2), dpToPx(0));
        // TODO: 03/06/2016 check commented functionality
//            Drawable drawable =
//                    ContextCompat.getDrawable(getContext(), R.drawable.amu_bubble_shadow);
//            iconGenerator.setBackground(drawable);
        iconGenerator.setBackground(
                new CustomBubbleDrawable(getResources(), getContext().getTheme()));
        Bitmap infoWindow = iconGenerator
                .makeIcon(StringUtils.getCurrencyString(price));


        return ViewUtil.createBigMarkerWithInfoWindow(
                getContext(),
                infoWindow,
                R.drawable.icon_marker_small);
    }

    private Bitmap clusterMarker(int units) {
        String text = String.valueOf(units);
        return ViewUtil.createSmallBitmapMarker(getContext(),
                R.drawable.icon_marker_normal,
                text);
    }

    @Override
    public void displayMarker(ClusterObject cluster) {
        LatLng latLng = new LatLng(
                cluster.getLat(),
                cluster.getLon());

        Bitmap markerBitmap;
        if (cluster.getUnits() == 1) {
            markerBitmap = singleMarker(cluster.getPrice());
        } else {
            markerBitmap = clusterMarker(cluster.getUnits());
        }

        BitmapDescriptor icon = BitmapDescriptorFactory.fromBitmap(
                markerBitmap);

        MarkerOptions markerOptions = new MarkerOptions()
                .icon(icon)
                .snippet(String.valueOf(cluster.getId()))
                .position(latLng);

        collection.addMarker(markerOptions);
    }


    @Override
    public void displayEstatesList(List<EstateModel> listings) {
        adapter.setItems(listings);
        adapter.notifyDataSetChanged();
        dialog.show();
    }

    @Override
    public void displayApartmentDetailsActivity(EstateModel estateModel) {
        startActivity(ApartmentDetailsActivity.getStartIntent(
                getContext(), estateModel, adapter.indexOf(estateModel)));
    }

    @Override
    public void changeZoom(float zoom) {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    @Override
    public void showError(String message) {
        actionView.showErrorView();
    }

    @Override
    public void showLoadingView() {
        actionView.showLoadingView();
    }

    @Override
    public void hideLoadingView() {
        actionView.hide();
    }

    @Override
    public void enableMapListener() {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        presenter.onHiddenChanged(hidden);
    }

    @Override
    public void clearMarkers() {
        collection.clear();
    }

    public void setCameraZoom(int zoom) {
        mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));
    }

    public void resetCamera() {
//        mMap.setOnCameraChangeListener();
    }


    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Timber.d("touchable is not null %s",touchableWrapper != null);

        if (enableCameraMove) {
            Timber.d("Markers camera change position zoom lvl %s", cameraPosition.zoom);
            presenter.saveCurrentLocationData(cameraPosition.target.latitude,
                    cameraPosition.target.longitude, mMap.getProjection());
            presenter.saveCameraZoom(cameraPosition.zoom);
            presenter.loadClustersWithZoom();
        }
    }*/
}
