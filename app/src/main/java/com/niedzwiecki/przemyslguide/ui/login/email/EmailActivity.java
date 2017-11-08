package com.niedzwiecki.przemyslguide.ui.login.email;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.databinding.ActivityEmailBinding;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.base.Navigator;
import com.niedzwiecki.przemyslguide.ui.login.password.PasswordActivity;
import com.niedzwiecki.przemyslguide.ui.main.MainActivity;


public class EmailActivity extends BaseActivity implements Navigator {

    EmailViewModel viewModel;

    private ActivityEmailBinding binding;

    public static void start(Context context) {
        Intent intent = new Intent(context, EmailActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new EmailViewModel();
//        activityComponent().inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_email);
        binding.setViewModel(viewModel);
//        viewModel.attachNavigator(this);
//        viewModel.checkIfUserIsAlreadyLogged();
    }

    @Override
    public int contentId() {
        return R.layout.activity_email;
    }

    @Override
    public void moveForward(Options options, Object... data) {
        super.moveForward(options, data);
        switch (options) {
            case START_PASSWORD_ACTIVITY:
                PasswordActivity.start(this, (String) data[0]);
                break;
            case START_MAIN_ACTIVITY:
                MainActivity.start(this, (String) data[0]);
        }
    }

}
