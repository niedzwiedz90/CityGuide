package com.niedzwiecki.przemyslguide.ui.login.password

import android.databinding.ObservableField
import android.databinding.ObservableInt
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.niedzwiecki.przemyslguide.R
import com.niedzwiecki.przemyslguide.data.DataManager
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel
import com.niedzwiecki.przemyslguide.ui.base.Navigator
import com.niedzwiecki.przemyslguide.util.Utils

class PasswordViewModel(internal var dataManager: DataManager) : BaseViewModel<PasswordActivity>() {
    var passwordFromEditText: ObservableField<String>
    var validationPasswordErrorText: ObservableField<String>
    var errorVisibility: ObservableInt
    var nextButtonVisibility: ObservableInt
    private var passwordEditText: String? = null
    private var emailAddress: String? = null
    private val error: ObservableField<String>

    val passwordTextWatcher: TextWatcher
        get() = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                passwordEditText = charSequence.toString()
                passwordFromEditText.set(passwordEditText)
                validPassword(passwordEditText)
            }

            override fun afterTextChanged(editable: Editable) {

            }
        }

    init {
        this.validationPasswordErrorText = ObservableField()
        this.error = ObservableField()
        this.passwordFromEditText = ObservableField()
        errorVisibility = ObservableInt(View.INVISIBLE)
        nextButtonVisibility = ObservableInt(View.GONE)
    }

    fun validPassword(password: CharSequence?) {
        if (Utils.isEmpty(password)) {
            showValidationErrorMessage(
                    dataManager.getResourcesManager().getString(
                            R.string.emptyPasswordValidation
                    )
            )
        } else if (!Utils.isPasswordLengthMinimumFourChars(password)) {
            showValidationErrorMessage(
                    dataManager.getResourcesManager().getString(R.string.notEnoughPasswordChars)
            )
        } else {
            nextButtonVisibility.set(View.VISIBLE)
            errorVisibility.set(View.INVISIBLE)
        }
    }

    private fun showValidationErrorMessage(errorMessage: String) {
        validationPasswordErrorText.set(errorMessage)
        errorVisibility.set(View.VISIBLE)
        nextButtonVisibility.set(View.GONE)
    }

    fun startMainActivity(view: View) {
        dataManager.storeAuthenticationHeader(emailAddress)
        getNavigator()!!.moveForward(Navigator.Options.START_MAIN_ACTIVITY, emailAddress)
        getNavigator()!!.finish()
    }

    fun sendEmailToVerification(email: String) {
        emailAddress = email
    }

    fun checkIfUserIsVerified() {
        if (isNavigatorAttached) {
            dataManager.storeAuthenticationHeader(emailAddress)
            getNavigator()!!.moveForward(Navigator.Options.START_MAIN_ACTIVITY, emailAddress)
        }
    }

    companion object {

        private val START_EMAIL_ACTIVITY = 0
        private val START_PASSWORD_ACTIVITY = 1
    }

}