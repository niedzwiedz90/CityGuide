package com.niedzwiecki.przemyslguide.ui.login.email;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.niedzwiecki.przemyslguide.data.DataManager;
import com.niedzwiecki.przemyslguide.data.StringManager;
import com.niedzwiecki.przemyslguide.data.local.PreferencesKeys;
import com.niedzwiecki.przemyslguide.ui.base.BaseViewModel;
import com.niedzwiecki.przemyslguide.ui.base.Navigator;
import com.niedzwiecki.przemyslguide.util.Utils;
import com.niedzwiecki.przemyslguide.util.ViewUtil;


public class EmailViewModel extends BaseViewModel {

    DataManager dataManager;

    public ObservableField<String> emailAddressFromEditText;
    public ObservableField<String> validationEmailAddressError;
    public ObservableInt errorVisibility;
    public ObservableInt nextButtonVisibility;
    private String emailText;

    public EmailViewModel(DataManager dataManager) {
        this.dataManager = dataManager;
        this.emailAddressFromEditText = new ObservableField<>();
        this.validationEmailAddressError = new ObservableField<>();
        errorVisibility = new ObservableInt(View.INVISIBLE);
        nextButtonVisibility = new ObservableInt(View.INVISIBLE);
    }

    public TextWatcher getEmailAddressTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                emailText = charSequence.toString();
                emailAddressFromEditText.set(emailText);
                validEmail(emailText);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public void onClickEmailAddress(View view) {
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        ViewUtil.showKeyboard(view);
    }

    public void validEmail(String email) {
        if (Utils.isEmpty(email)) {
            showValidationErrorMessage(dataManager.getResourcesManager().
                    getString(StringManager.ERROR_EMPTY_EMAIL_ADDRESS_VALIDATION));
        } else if (!Utils.isValidEmail(email)) {
            showValidationErrorMessage(dataManager.getResourcesManager().
                    getString(StringManager.ERROR_INVALID_EMAIL_ADDRESS));
        } else if (Utils.isValidEmail(email)) {
            nextButtonVisibility.set(View.VISIBLE);
            validationEmailAddressError.set("");
            errorVisibility.set(View.INVISIBLE);
        }
    }

    public void showValidationErrorMessage(String errorMessage) {
        validationEmailAddressError.set(errorMessage);
        errorVisibility.set(View.VISIBLE);
        nextButtonVisibility.set(View.INVISIBLE);
    }

    public void startPasswordActivity(View view) {
        checkNavigatorAttached();
        getNavigator().moveForward(Navigator.Options.START_PASSWORD_ACTIVITY, emailText);
        getNavigator().finish();
    }

    public void checkIfUserIsAlreadyLogged() {
        if (dataManager.contains(PreferencesKeys.LOGION_HEADER)) {
            getNavigator().moveForward(Navigator.Options.START_MAIN_ACTIVITY,
                    dataManager.getPreferencesHelper().getAuthenticationHeader(PreferencesKeys.LOGION_HEADER));
            getNavigator().finish();
        }

    }
}
