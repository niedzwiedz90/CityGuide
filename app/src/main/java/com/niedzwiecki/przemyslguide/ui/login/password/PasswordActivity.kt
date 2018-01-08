package com.niedzwiecki.przemyslguide.ui.login.password

import android.app.Activity
import android.content.Intent
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.databinding.ActivityPasswordBinding
import com.niedzwiecki.przemyslguide.ui.base.BaseActivity
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.ui.base.ViewModel
import com.niedzwiecki.przemyslguide.ui.main.MainActivity

class PasswordActivity : BaseActivity(), Navigator {

    override fun beforeViews() {
        super.beforeViews()
        setDataBindingEnabled(true)
    }

    override fun afterViews() {
        super.afterViews()
        viewDataBinding.viewModel = getViewModel()
        val email = intent.getStringExtra(EMAIL_KEY)
        getViewModel().sendEmailToVerification(email)
    }

    override fun contentId(): Int {
        return R.layout.activity_password
    }

    override fun moveForward(options: Navigator.Options, vararg data: Any) {
        super.moveForward(options, *data)
        when (options) {
            Navigator.Options.START_MAIN_ACTIVITY -> MainActivity.start(this, data[0] as String)
        }
    }

    override fun createViewModel(): ViewModel? {
        return PasswordViewModel(dataManager)
    }

    override fun getViewModel(): PasswordViewModel {
        return super.getViewModel() as PasswordViewModel
    }

    override fun getViewDataBinding(): ActivityPasswordBinding {
        return super.getViewDataBinding() as ActivityPasswordBinding
    }

    companion object {

        val EMAIL_KEY = "emailKey"

        fun start(context: Activity, email: String) {
            val starter = Intent(context, PasswordActivity::class.java)
            starter.putExtra(EMAIL_KEY, email)
            context.startActivity(starter)
            context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}

