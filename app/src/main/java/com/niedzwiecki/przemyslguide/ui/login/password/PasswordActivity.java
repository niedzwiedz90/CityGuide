package com.niedzwiecki.przemyslguide.ui.login.password;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.databinding.ActivityPasswordBinding;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.base.Navigator;
import com.niedzwiecki.przemyslguide.ui.base.ViewModel;
import com.niedzwiecki.przemyslguide.ui.main.MainActivity;

public class PasswordActivity extends BaseActivity implements Navigator {

    public static final String EMAIL_KEY = "emailKey";

    public static void start(Activity context, String email) {
        Intent starter = new Intent(context, PasswordActivity.class);
        starter.putExtra(EMAIL_KEY, email);
        context.startActivity(starter);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActivityPasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_password);
//        binding.setViewModel(viewModel);
        Intent intent = getIntent();
//        String email = intent.getStringExtra(EMAIL_KEY);
    }
*/

    @Override
    public void beforeViews() {
        super.beforeViews();
        setDataBindingEnabled(true);
    }

    @Override
    public void afterViews() {
        super.afterViews();
        getViewDataBinding().setViewModel(getViewModel());
        String email = getIntent().getStringExtra(EMAIL_KEY);
        getViewModel().sendEmailToVerification(email);
    }

    @Override
    public int contentId() {
        return R.layout.activity_password;
    }

    @Override
    public void moveForward(Options options, Object... data) {
        super.moveForward(options, data);
        switch (options) {
            case START_MAIN_ACTIVITY:
                MainActivity.Companion.start(this, (String) data[0]);
                break;
        }
    }

    @Override
    public ViewModel createViewModel() {
        return new PasswordViewModel(dataManager);
    }

    @Override
    public PasswordViewModel getViewModel() {
        return (PasswordViewModel) super.getViewModel();
    }

    @Override
    public ActivityPasswordBinding getViewDataBinding() {
        return (ActivityPasswordBinding) super.getViewDataBinding();
    }
}

