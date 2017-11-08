package com.niedzwiecki.przemyslguide.ui.login.password;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.StringManager;
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel;
import com.niedzwiecki.przemyslguide.ui.base.Navigator;
import com.niedzwiecki.przemyslguide.util.Utils;

import javax.inject.Inject;

import rx.Subscription;

public class PasswordViewModel extends BaseViewModel {

    private static final int START_EMAIL_ACTIVITY = 0;
    private static final int START_PASSWORD_ACTIVITY = 1;

    DataManager dataManager;
    public ObservableField<String> passwordFromEditText;
    public ObservableField<String> validationPasswordErrorText;
    public ObservableInt errorVisibility;
    public ObservableInt nextButtonVisibility;
    private String passwordEditText;
    private String emailAddress;
    private Subscription mSubscrition;
    private ObservableField<String> error;

    @Inject
    public PasswordViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.validationPasswordErrorText = new ObservableField<>();
        this.error = new ObservableField<>();
        this.passwordFromEditText = new ObservableField<>();
        errorVisibility = new ObservableInt(View.INVISIBLE);
        nextButtonVisibility = new ObservableInt(View.GONE);
    }

    public TextWatcher getPasswordTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordEditText = charSequence.toString();
                passwordFromEditText.set(passwordEditText);
                validPassword(passwordEditText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public void validPassword(CharSequence password) {
        if (Utils.isEmpty(password)) {
            showValidationErrorMessage(dataManager.
                    getResourcesManager().getString(StringManager.ERROR_EMPTY_PASSWORD_VALIDATION));
        } else if (!Utils.isPasswordLengthMinimumFourChars(password)) {
            showValidationErrorMessage(dataManager.
                    getResourcesManager().getString(StringManager.ERROR_NOT_ENOUGH_PASSWORD_CHARS));
        } else {
            nextButtonVisibility.set(View.VISIBLE);
            errorVisibility.set(View.INVISIBLE);
        }
    }

    private void showValidationErrorMessage(String errorMessage) {
        validationPasswordErrorText.set(errorMessage);
        errorVisibility.set(View.VISIBLE);
        nextButtonVisibility.set(View.GONE);
    }

    public void startMainActivity(View view) {
        dataManager.storeAuthenticationHeader(emailAddress);
        getNavigator().moveForward(Navigator.Options.START_MAIN_ACTIVITY, emailAddress);
        getNavigator().finish();
    }

    public void sendEmailToVerification(String email) {
        emailAddress = email;
    }

    public void checkIfUserIsVerified() {
        if (isNavigatorAttached()) {
            dataManager.storeAuthenticationHeader(emailAddress);
            getNavigator().moveForward(Navigator.Options.START_MAIN_ACTIVITY, emailAddress);
        }
    }

}