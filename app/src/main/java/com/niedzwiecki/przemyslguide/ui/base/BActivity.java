package com.niedzwiecki.przemyslguide.ui.base;

import android.support.v7.app.AppCompatActivity;

public abstract class BActivity extends AppCompatActivity implements Navigator, ViewModelIntegration {

/*
    public static final String BEACON_ACTION = "BEACON_ACTION";
    private static boolean inForeground = false; // FIXME: 10/08/2016 check if name is correct, this is static and all of activities has the same field.
    private static final String VERSION_PREFS_UPDATE_NEEDED = "UpdateNeeded";
    private static final String VERSION_PREFS_LAST_VERSION_TO_UPDATE = "LastVersionToUpdate";
    public static final int SNACKBAR_VISIBILITY_DURATION = 5000;
    private ProgressDialog progressDialog;

    public DataManager dataManager;

//    @Nullable
//    @BindView(R.id.toolbar)w
    Toolbar toolbar;

    private ViewModel viewModel;
    private int currentVersion;
    private boolean dataBindingEnabled = false;
    private ViewDataBinding viewDataBinding;

    private SharedPreferences prefs;

    BeaconActionReceiver beaconActionReceiver = null;
//    private Snackbar snackbar;
    private Handler dismissHandler;
    private Runnable dismissRunnable;
    private Handler handler;
    private Runnable runnable;
    private HandleNotificationHelper notificationHelper;
    private Drawable background;
    private WeakReference<BActivity> weakReference;
    private boolean avoidOfflineMessages;
    private boolean showHomeAsUp = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        weakReference = new WeakReference<>(this);
        super.onCreate(savedInstanceState);
        dataManager = ApplicationController.getInstance().getDataModule().provideDataManager();
        prefs = getSharedPreferences(PreferencesManager.VERSION_PREFS, Context.MODE_PRIVATE);
        currentVersion = BuildConfig.VERSION_CODE;

        beforeViews();
        if (contentId() != 0 && !dataBindingEnabled) {
            setContentView(contentId());
        } else if (contentId() != 0 && dataBindingEnabled) {
            viewDataBinding = DataBindingUtil.setContentView(this, contentId());
        } else {
            Timber.d("You didn't setup content layout at: %s", getClass().getName());
        }

        if (dataBindingEnabled) {
            viewModel = viewModel != null ? viewModel : createViewModel();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setupToolbarWithDataBinding(toolbar, showHomeAsUp);
        } else {
            ButterKnife.bind(this);
            setupToolbar();
        }

        if (savedInstanceState == null) {
            afterViews();
        } else {
            if (getViewModel() != null) {
                getViewModel().restoreInstanceState(savedInstanceState);
            }

            afterViews(savedInstanceState);
        }

        notificationHelper = new HandleNotificationHelper(weakReference);
        if (viewModel != null) {
            viewModel.onCreate();
        }
    }

    protected void afterViews(Bundle savedInstanceState) {

    }

    public void beforeViews() {

    }

    public void afterViews() {

    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        getSupportActionBar().setTitle(titleId);
    }

    private void setupToolbarWithDataBinding(Toolbar toolbar, boolean showHomeAsUp) {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() == null) {
                return;
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            if(showHomeAsUp){
                getSupportActionBar().setHomeAsUpIndicator(Utils.getCustomArrowIcon(this));
            }
        } else {
            return;
        }
    }

    public void setShowHomeAsUp(boolean showHomeAsUp) {
        this.showHomeAsUp = showHomeAsUp;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public abstract int contentId();

    public void setDataBindingEnabled(boolean dataBindingEnabled) {
        this.dataBindingEnabled = dataBindingEnabled;
    }

    public ViewDataBinding getViewDataBinding() {
        return viewDataBinding;
    }

    @Deprecated
    public void setupToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(Utils.getCustomArrowIcon(this));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataManager.sendStat();

        boolean updateInfoInPrefs = isUpdateInfoInPrefs();
        inForeground = true;
        beaconActionReceiver = new BeaconActionReceiver();
        registerReceiver(beaconActionReceiver, new IntentFilter(BeaconsMonitoringService.BROADCAST_NAME));

        if (updateInfoInPrefs) {
            int lastKnownVersionToUpdate = getLastKnownVersionFromPrefs();

            if (currentVersion == lastKnownVersionToUpdate) {
                launchUpdateNeededActivity();
            } else {
                clearPrefs();
            }
        } else {
            boolean isOkToCheck = PreferencesManager.isOkToCheckCompatibility();

            if (isOkToCheck) {
                check();
            }
        }
        notificationHelper.register();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (viewModel != null) {
            viewModel.attachNavigator(this);
        }

        Utils.hideKeyboard(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (viewModel != null) {
            viewModel.detachNavigator();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(beaconActionReceiver);
        inForeground = false;
        notificationHelper.unregister();
    }

    private void clearPrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(VERSION_PREFS_LAST_VERSION_TO_UPDATE);
        editor.remove(VERSION_PREFS_UPDATE_NEEDED);
        editor.commit();
    }

    private void check() {
        dataManager.checkVersionCompatibility()
                .onErrorReturn(throwable -> null)
                .doOnNext(apiVersionResponse -> {
                    if (apiVersionResponse == null) {
                        return;
                    }

                    boolean doesNeedUpdate = !apiVersionResponse.compatible;
                    if (doesNeedUpdate) {
                        updatePrefs();
                        launchUpdateNeededActivity();
                    }
                }).subscribe();
        PreferencesManager.markCompatibilityCheckTime();
    }

    private boolean isUpdateInfoInPrefs() {
        return prefs.getBoolean(VERSION_PREFS_UPDATE_NEEDED, false);
    }

    private int getLastKnownVersionFromPrefs() {
        return prefs.getInt(VERSION_PREFS_LAST_VERSION_TO_UPDATE, -1);
    }

    private void updatePrefs() {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(VERSION_PREFS_LAST_VERSION_TO_UPDATE, BuildConfig.VERSION_CODE);
        editor.putBoolean(VERSION_PREFS_UPDATE_NEEDED, true);
        editor.commit();
    }

    private void launchUpdateNeededActivity() {
        Intent intent = Routing.getUpdateNeededActivity(this);
        startActivity(intent);
        finish();
    }

    public static boolean isInForeground() {
        return inForeground;
    }

    @Override
    public void moveForward(Options options, Object... data) {

    }

    @Override
    public void goBack() {
        onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        Utils.hideKeyboard(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (viewModel != null) {
            viewModel.onDestroy();
            viewModel = null;
        }

        notificationHelper = null;
        prefs = null;
        if (viewDataBinding != null) {
            viewDataBinding.unbind();
        }


        viewDataBinding = null;
    }

    @Override
    public ViewModel createViewModel() {
        return null;
    }

    public void setAvoidOfflineMessages(boolean avoidOfflineMessages) {
        this.avoidOfflineMessages = avoidOfflineMessages;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public static class BeaconActionReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            onEventBeaconActionReceived(context, intent);
        }

    }

    public static void onEventBeaconActionReceived(Context context, Intent intent) {
        EventoryBeaconAction beaconAction = (EventoryBeaconAction) intent.getSerializableExtra(BEACON_ACTION);
        new BeaconActionDialog(context).showDialogForAction(beaconAction);
    }

    protected void showErrorConnectionSnackbar() {
        if (avoidOfflineMessages) {
            return;
        }

        showSnackbar(R.string.offline_mode,
                R.string.RETRY,
                v -> {
                    NotificationController.postSafe(NotificationController.REFRESH_DATA);
                    if (Utils.isNetworkConnected(v.getContext())) {
                        snackbar.dismiss();
                    } else {
                        new Handler().postDelayed(() ->
                                new Handler(getMainLooper())
                                        .post(() -> {
                                            if (dismissHandler != null) {
                                                dismissHandler.removeCallbacks(dismissRunnable);
                                            }
                                            snackbar.show();
                                            dismissSnackbarAfterDelay();
                                        }), 500);
                    }
                });
    }

    protected void showSnackbar(@StringRes int messageRes, @StringRes int actionButtonText, View.OnClickListener actionListenner) {
        showSnackbar(getString(messageRes), getString(actionButtonText), actionListenner);
    }

    protected void showSnackbar(String message, String actionButtonText, View.OnClickListener actionListenner) {
        if (dismissHandler != null) {
            dismissHandler.removeCallbacks(dismissRunnable);
        }

        View coordinatorLayout = findViewById(R.id.coordinatorLayout);
        if (coordinatorLayout == null) {
            coordinatorLayout = findViewById(R.id.container);
        }

        if (coordinatorLayout == null) {
            coordinatorLayout = findViewById(R.id.toolbar);
        }

        if (coordinatorLayout == null) {
            Log.e("Snackbar", String.format("No container or coordinatorLayout at class %s", getClass().getSimpleName()));
            Toast.makeText(ApplicationController.getInstance(), message, Toast.LENGTH_SHORT).show();
        } else {
            if (snackbar == null) {
                if (handler == null) {
                    handler = new Handler();
                    runnable = () -> snackbar.dismiss();
                }

                snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_INDEFINITE);
                background = snackbar.getView().getBackground();
                snackbar.setAction(actionButtonText, actionListenner);
            }

            if (!snackbar.isShown()) {
                snackbar.show();
            }

            dismissSnackbarAfterDelay();
        }
    }

    private void dismissSnackbarAfterDelay() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, SNACKBAR_VISIBILITY_DURATION);
    }

    protected void showProgressDialog() {
        showProgress(R.string.processing);
    }

    protected void showProgress(@StringRes int messageResource) {
        showProgress(dataManager.getString(messageResource));
    }

    @Override
    public void showProgress(String message) {
        if (isFinishing() || progressDialog != null) {
            return;
        }
        progressDialog = DialogFactory.createProgressDialog(this, message);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        if (progressDialog == null) {
            return;
        }
        try {
            progressDialog.dismiss();
        } catch (Exception e) {
            Timber.e(e.getMessage());
        }
        progressDialog = null;
    }

    @Override
    public void showError(@NonNull String content) {
        boolean showed = DialogFactory.safeShowDialog(this, DialogFactory.createGenericErrorDialog(this, content));
        if (!showed) {
            dataManager.showToast(content, Toast.LENGTH_LONG);
        }
    }

    @Override
    public void showError(@NonNull String title, @NonNull String content) {
        boolean showed = DialogFactory.safeShowDialog(this, DialogFactory.createSimpleOkErrorDialog(this, title, content));
        if (!showed) {
            dataManager.showToast(String.format(Locale.getDefault(), "%s: %s", title, content), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void showInfo(@NonNull String title, @NonNull String content, @NonNull String positiveButtonText, DialogInterface.OnClickListener positiveClick) {
        boolean showed = DialogFactory.safeShowDialog(this, DialogFactory.createPositiveDialog(this, title, content, positiveButtonText, positiveClick));
        if (!showed) {
            dataManager.showToast(String.format(Locale.getDefault(), "%s: %s", title, content), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void showInfo(@NonNull String title,
                         @NonNull String content,
                         @NonNull String positiveButtonText,
                         DialogInterface.OnClickListener positiveClick,
                         @NonNull String negativeButtonText,
                         DialogInterface.OnClickListener negativeClick) {
        boolean showed = DialogFactory.safeShowDialog(this,
                DialogFactory.createInformDialog(this,
                        title,
                        content,
                        positiveButtonText, positiveClick,
                        negativeButtonText, negativeClick));
        if (!showed) {
            dataManager.showToast(String.format(Locale.getDefault(), "%s: %s", title, content), Toast.LENGTH_LONG);
        }
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, Serializable serializable) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(Navigator.DEFAULT_ARG_KEY, serializable);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, String key, Serializable serializable) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(key, serializable);
        startActivity(intent);
    }

    @Override
    public void startActivity(Class<? extends Activity> activityClass, Bundle bundle) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, Serializable serializable, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(Navigator.DEFAULT_ARG_KEY, serializable);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, String key, Serializable serializable, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtra(key, serializable);
        startActivityForResult(intent, requestCode);
    }

    @Override
    public void startActivityForResult(Class<? extends Activity> activityClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, activityClass);
        intent.putExtras(bundle);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getViewModel() != null) {
            getViewModel().onActivityResult(this, requestCode, resultCode, data == null ? null : data.getExtras());
        }
    }

    @Override
    public void startWebSiteFromUrl(String urlAddress) {
        Utils.openWebPageWithCheckPackage(this
                        .getApplicationContext(),
                urlAddress);
    }



    class HandleNotificationHelper {

        private final WeakReference<BActivity> weakReference;

        public HandleNotificationHelper(WeakReference<BActivity> weakReference) {
            this.weakReference = weakReference;
        }

        public void register() {
            NotificationController.register(HandleNotificationHelper.this);
        }

        public void unregister() {
            NotificationController.unregister(HandleNotificationHelper.this);
        }

        @Subscribe
        public void didReceiveNotification(NotificationController.Event event) {
            BActivity bActivity = weakReference.get();
            if (bActivity != null) {
                bActivity.didReceiveNotification(event);
            }
        }
    }

    private void didReceiveNotification(NotificationController.Event event) {
        // TODO: 17/11/2016 check if snackbar is from error network.
        switch (event.id) {
            case NotificationController.NETWORK_ERROR:
                AndroidComponentUtil.toggleComponent(BActivity.this, SyncOnConnectionAvailable.class, true);
                showErrorConnectionSnackbar();

                if (snackbar != null) {
                    Utils.setBackgroundDrawable(snackbar.getView(), background);
                    ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setText(getString(R.string.offline_mode));
                    snackbar.getView().findViewById(R.id.snackbar_action).setVisibility(View.VISIBLE);
                }
                break;
            case NotificationController.NETWORK_CONNECTED:
                if (snackbar != null) {
                    // TODO: 17/11/2016 animate color
//                        snackbar.getView().animateColorBackground();
                    snackbar.getView().setBackgroundResource(R.color.newGreen);
                    ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setText(getString(R.string.network_connected));
                    snackbar.getView().findViewById(R.id.snackbar_action).setVisibility(View.GONE);
                    snackbar.show();
                    dismissSnackbarAfterDelay();
                } else {
                    showSnackbar(getString(R.string.network_connected), "", v -> {
                        //do nothing
                        //todo get action from event if needed
                    });

                    if (snackbar != null) {
                        snackbar.getView().setBackgroundResource(R.color.newGreen);
                        ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setText(getString(R.string.network_connected));
                        snackbar.getView().findViewById(R.id.snackbar_action).setVisibility(View.GONE);
                        snackbar.show();
                        dismissSnackbarAfterDelay();
                    }
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (getViewModel() != null) {
            getViewModel().saveInstanceState(outState);
        }
    }
*/

}