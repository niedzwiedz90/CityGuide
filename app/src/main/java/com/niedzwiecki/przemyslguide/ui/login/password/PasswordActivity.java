package com.niedzwiecki.przemyslguide.ui.login.password;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.Button;

import com.niedzwiecki.przemyslguide.R;
import com.niedzwiecki.przemyslguide.databinding.ActivityPasswordBinding;
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity;
import com.niedzwiecki.przemyslguide.ui.base.Navigator;
import com.niedzwiecki.przemyslguide.ui.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordActivity extends BaseActivity implements Navigator {

    public static final String EMAIL_KEY = "emailKey";

    PasswordViewModel viewModel;

    @BindView(R.id.nextButton)
    Button nextButton;

    public static void start(Activity context, String email) {
        Intent starter = new Intent(context, PasswordActivity.class);
        starter.putExtra(EMAIL_KEY, email);
        context.startActivity(starter);
        context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityPasswordBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_password);
        viewModel = new PasswordViewModel(dataManager);
        binding.setViewModel(viewModel);
        viewModel.attachNavigator(this);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        String email = intent.getStringExtra(EMAIL_KEY);
        viewModel.sendEmailToVerification(email);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        viewModel.detachNavigator();
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

}

