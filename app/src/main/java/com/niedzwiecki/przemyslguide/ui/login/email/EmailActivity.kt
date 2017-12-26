package com.niedzwiecki.przemyslguide.ui.login.email

import android.content.Context
import android.content.Intent
import android.os.Bundle

import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.databinding.ActivityEmailBinding
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.ui.login.password.PasswordActivity
import com.niedzwiecki.przemyslguide.ui.main.MainActivity


class EmailActivity : BaseActivity(), Navigator {
    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        viewModel = new EmailViewModel(dataManager);
//        viewModel.attachNavigator(this);
//        viewModel.checkIfUserIsAlreadyLogged();
    }*/

    override fun beforeViews() {
        super.beforeViews()
        setDataBindingEnabled(true)
    }

    override fun afterViews(savedInstanceState: Bundle) {
        super.afterViews(savedInstanceState)
        viewDataBinding.viewModel = getViewModel()
    }

    override fun afterViews() {
        super.afterViews()
        viewDataBinding.viewModel = getViewModel()
    }

    override fun getViewDataBinding(): ActivityEmailBinding {
        return super.getViewDataBinding() as ActivityEmailBinding
    }

    override fun getViewModel(): EmailViewModel {
        return super.getViewModel() as EmailViewModel
    }

    override fun createViewModel(): EmailViewModel? {
        return EmailViewModel(dataManager)
    }

    override fun contentId(): Int {
        return R.layout.activity_email
    }

    override fun moveForward(options: Navigator.Options, vararg data: Any) {
        super.moveForward(options, *data)
        when (options) {
            Navigator.Options.START_PASSWORD_ACTIVITY -> PasswordActivity.start(this, data[0] as String)
            Navigator.Options.START_MAIN_ACTIVITY -> MainActivity.start(this, data[0] as String)
        }
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, EmailActivity::class.java)
            context.startActivity(intent)
        }
    }

}
