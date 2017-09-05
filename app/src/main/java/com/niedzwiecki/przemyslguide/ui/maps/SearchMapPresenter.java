package com.niedzwiecki.przemyslguide.ui.maps;

public class SearchMapPresenter {

/*
    DataManager dataManager;
    private Subscription mSubscription;
    private List<ClusterObject> clusters;
    private boolean loadDataFirstTimeAfterAttach;
    private Observable lastObservable;
    private List<EstateModel> apartments;
    private boolean shouldLoadClusters;
    private float correctionMap;
    private int offset;
    private boolean zoomEnable;
    private LocationHelper locationHelper;

    @Inject
    public SearchMapPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
        this.offset = dataManager.getZoomOffset();
    }

    @Override
    public void attachView(SearchMapMvpView mvpView) {
        super.attachView(mvpView);
        loadDataFirstTimeAfterAttach = true;
        dataManager.registerForEvents(this);
    }

    @Override
    public void detachView() {
        super.detachView();
        dataManager.unregisterForEvents(this);
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    @Subscribe
    public void didReceiveEvent(Event event) {
        if ((event.id == EventPosterHelper.FILTERS_CHANGED)) {
            if (hasLocationChange()) {
                // Don't move camera if the location didn't change
                getMvpView().moveCamera(
                        dataManager.getLatMin(),
                        dataManager.getLonMin(),
                        dataManager.getLatMax(),
                        dataManager.getLonMax());

                if (isViewAttached()) {
                    getMvpView().clearMarkers();
                    getMvpView().showLoadingView();
                }
            } else {
                loadClustersWithZoom();
            }
        }
    }

    @Override
    public void init() {
        super.init();
        if (hasLocationChange()) {
            getMvpView().moveCamera(
                    dataManager.getLatMin(),
                    dataManager.getLonMin(),
                    dataManager.getLatMax(),
                    dataManager.getLonMax());
            if (isViewAttached()) {
                getMvpView().clearMarkers();
                getMvpView().showLoadingView();
                getMvpView().enableCameraListener(false);
            }
        } else if(clusters != null) {
            getMvpView().clearMarkers();
            for (ClusterObject cluster : clusters) {
                getMvpView().displayMarker(cluster);
            }
            getMvpView().enableCameraListener(true);
            getMvpView().hideLoadingView();
        }
    }

    boolean hasLocationChange() {
        LocationHelper current = new LocationHelper(dataManager.getLatMin(),
                dataManager.getLonMin(),
                dataManager.getLatMax(),
                dataManager.getLonMax());
        return locationHelper == null ||(!locationHelper.equals(current));
    }

    //TODO replace 11 and 2 by properly value from DataManager
    private SearchPayload getSearchPayLoad() {
        SearchPayload payload = dataManager.getClustersSearchPayload();
        payload.zoom = zoomEnable ? (int) (dataManager.getZoom() + dataManager
                .getZoomOffset()) : (int) dataManager.getZoom();
        //payload.zoom = dataManager.getCameraZoom() + 2;
        return payload;
    }

    public Subscription loadClusters(SearchPayload searchPayload) {
        return dataManager
                .getClusters(new Gson().toJson(searchPayload), false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ClusterObject>>() {
                    @Override
                    public void call(List<ClusterObject> clusterObjects) {
                        if (isViewAttached()) {
                            getMvpView().hideLoadingView();
                            getMvpView().clearMarkers();
                            Timber.d("Markers clusters size %s", clusterObjects.size());
                            for (ClusterObject cluster : clusterObjects) {
                                getMvpView().displayMarker(cluster);
                            }
                            clusters = clusterObjects;
                            getMvpView().enableCameraListener(true);
                            dataManager.postNotification(EventPosterHelper.ZOOM_CHANGED);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Timber.e("Error retrieve on clusters %s", throwable.toString());
                    }
                });
    }

    public void saveCurrentLocationData(double latitude, double longitude, Projection projection) {
        Timber.d("Markers update location bounds");
        dataManager.storeCenterLat(latitude);
        dataManager.storeCenterLon(longitude);
        dataManager.storeLatMax(projection.getVisibleRegion().latLngBounds.northeast.latitude);
        dataManager.storeLatMin(projection.getVisibleRegion().latLngBounds.southwest.latitude);
        dataManager.storeLonMax(projection.getVisibleRegion().latLngBounds.northeast.longitude);
        dataManager.storeLonMin(projection.getVisibleRegion().latLngBounds.southwest.longitude);
        locationHelper = new LocationHelper(dataManager.getLatMin(),
                dataManager.getLonMin(),
                dataManager.getLatMax(),
                dataManager.getLonMax());
    }

    public void saveCameraZoom(float zoom) {
        dataManager.storeZoom(zoom);
    }

    public void loadClustersWithZoom() {
        if (isViewAttached()) {
            getMvpView().showLoadingView();
            getMvpView().clearMarkers();
            getMvpView().enableCameraListener(false);
        }
        SearchPayload payload = getSearchPayLoad();
        //payload.zoom = dataManager.getCameraZoom() + 2;
        payload.zoom = zoomEnable ? (int) (dataManager.getZoom() + dataManager
                .getZoomOffset()) : (int) dataManager.getZoom();
        subscription.add(loadClusters(payload));
    }

    public void loadClusters(final boolean moveCamera, boolean forceRefresh) {
        this.loadClusters(moveCamera, forceRefresh, false);
    }

    public void loadClusters(final boolean moveCamera, boolean forceRefresh, final boolean
            enableZoom) {
        this.zoomEnable = enableZoom;
        checkViewAttached();
        getMvpView().updateLabels();
        if (getMvpView().isHidden()) {
            shouldLoadClusters = true;
            return;
        }

        if (!forceRefresh && loadDataFirstTimeAfterAttach && clusters != null) {
            loadDataFirstTimeAfterAttach = false;
            getMvpView().clearMarkers();
            for (ClusterObject cluster : clusters) {
                getMvpView().displayMarker(cluster);
            }
            if (moveCamera) {
                getMvpView().moveCamera(
                        dataManager.getCenterLon(),
                        dataManager.getCenterLat(),
                        dataManager.getZoom() + offset);
            }
            getMvpView().hideLoadingView();
            getMvpView().enableMapListener();
            return;
        }

        getMvpView().showLoadingView();

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        SearchPayload payload = dataManager.getClustersSearchPayload();
        payload.zoom = enableZoom ? dataManager.loadAutoCompleteFilter().iosZoom + dataManager
                .getZoomOffset() : (int) dataManager.getZoom();
        Timber.d("UPDATES %s", payload.toString());
//        dataManager.storeZoom(payload.zoom + dataManager.getZoomOffset());
        Observable<List<ClusterObject>> listObservable = dataManager
                .getClusters(new Gson().toJson(payload), !forceRefresh)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, List<ClusterObject>>() {
                    @Override
                    public List<ClusterObject> call(Throwable throwable) {
                        getMvpView().showError(throwable.getMessage());
                        return null;
                    }
                })
                .doOnNext(new Action1<List<ClusterObject>>() {
                    @Override
                    public void call(List<ClusterObject> estateResponseModel) {
                        if (estateResponseModel == null) {
                            return;
                        }
                        loadDataFirstTimeAfterAttach = false;
                        getMvpView().hideLoadingView();
                        clusters = estateResponseModel;

                        getMvpView().clearMarkers();
                        Timber.d("Clusters count: %d", clusters.size());
                        for (ClusterObject cluster : clusters) {
                            getMvpView().displayMarker(cluster);
                        }

                        if (moveCamera) {
                            if (getMvpView().isHidden()) {
                                shouldLoadClusters = true;
                                return;
                            }
                            getMvpView().moveCamera(
                                    dataManager.getCenterLon(),
                                    dataManager.getCenterLat(),
                                    dataManager.getZoom());
                        }
                        zoomEnable = false;
                        getMvpView().enableMapListener();
                    }
                });

        lastObservable = listObservable;
        mSubscription = listObservable.subscribe();
    }

    public void onCameraChanged(float zoom,
                                double centerLat, double centerLon,
                                double northeastLatitude,
                                double northeastLongitude,
                                double southwestLatitude,
                                double southwestLongitude) {
        long round = Math.round(zoom + correctionMap);
        if (zoom < 6) {
            getMvpView().changeZoom(6);
            return;
        } else if (zoom > 18) {
            getMvpView().changeZoom(18);
            return;
        }
        Timber.d("UPDATES on camera change");
        dataManager.storeCenterLat(centerLat);
        dataManager.storeCenterLon(centerLon);
        dataManager.storeZoom(round);
        dataManager.storeLatMax(northeastLatitude);
        dataManager.storeLatMin(southwestLatitude);
        dataManager.storeLonMax(northeastLongitude);
        dataManager.storeLonMin(southwestLongitude);
        if (!zoomEnable) {
            loadClusters(false, true);
        }
        getMvpView().updateLabels();
        dataManager.clearListingCache();
    }

    public void onMarkerClicked(Marker marker, int zoom) {
        for (ClusterObject cluster : clusters) {
            if (cluster.getId() == Long.parseLong(marker.getSnippet())
                    && cluster.getUnits() < 11) {
                getMvpView().showBottomListItems(marker, zoom);
                return;
            }
        }
//
        getMvpView().zoomToMarker(marker);
    }

    public void loadListEstates(long l) {
        ClusterObject localCluster = null;
        for (ClusterObject cluster : clusters) {
            if (l == cluster.getId()) {
                localCluster = cluster;
            }
        }

        if (localCluster == null) {
            Timber.e("Clicked cluster should be found in cluster by snippets id");
            return;
        }

        getMvpView().showLoadingView();
        SearchPayload searchPayload = dataManager.getSearchMapPayload(localCluster.getId());
        Observable<EstateResponseModel> estateResponseModelObservable = dataManager
                .getEstates(-1, new Gson().toJson(searchPayload), false, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, EstateResponseModel>() {
                    @Override
                    public EstateResponseModel call(Throwable throwable) {
                        getMvpView().showError(throwable.getMessage());
                        return null;
                    }
                }).doOnNext(new Action1<EstateResponseModel>() {
                    @Override
                    public void call(EstateResponseModel estateResponseModel) {
                        if (estateResponseModel == null) {
                            return;
                        }

                        getMvpView().hideLoadingView();
                        apartments = estateResponseModel.getListings().getRows();
                        getMvpView().displayEstatesList(apartments);
                    }
                });
        lastObservable = estateResponseModelObservable;
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        mSubscription = estateResponseModelObservable.subscribe();
    }

    public void onErrorClicked() {
        mSubscription = lastObservable.subscribe();
    }

    public void onBottomSheetItemClicked(int position) {
        getMvpView().displayApartmentDetailsActivity(apartments.get(position));
    }

    public void onHiddenChanged(boolean hidden) {
        Timber.d("UPDATES hidden update map");
        if (!hidden && shouldLoadClusters) {
            Timber.d("UPDATES load hidden update map");
            shouldLoadClusters = false;
            getMvpView().moveCamera(
                    dataManager.getLatMin(),
                    dataManager.getLonMin(),
                    dataManager.getLatMax(),
                    dataManager.getLonMax()
            );
            loadClusters(false, false, false);
        }
    }

    public void onTextChanged(String s) {
        if (StringUtils.isEmpty(s)) {
            dataManager.setCorrectionZoom(0);
        } else {
            try {
                dataManager.setCorrectionZoom(Float.parseFloat(s));
            } catch (NumberFormatException e) {
                Timber.e(e, "Error on parse correction number");
                dataManager.setCorrectionZoom(0);
            }
        }
    }

    public void mapCorrection(String s) {
        if (StringUtils.isEmpty(s)) {
            correctionMap = 0;
        } else {
            try {
                correctionMap = Float.parseFloat(s);
            } catch (NumberFormatException e) {
                Timber.e(e, "Error on parse correction number");
                correctionMap = 0;
            }
        }
    }

    public void onMapReady(final boolean hidden) {
        // workaround need investigation
        // fragment is visible for a moment after and we can't do any action on map
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (!hidden) {
//                    getMvpView().moveCamera(
//                            dataManager.getLatMin(),
//                            dataManager.getLonMin(),
//                            dataManager.getLatMax(),
//                            dataManager.getLonMax()
//                    );
//                }
//            }
//        }, 10);
    }
*/


}
