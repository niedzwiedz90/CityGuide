package com.niedzwiecki.przemyslguide.data;

import android.app.Application;

import com.niedzwiecki.przemyslguide.R;

import javax.inject.Inject;

/**
 * Created by niedzwiedz on 21.08.17.
 */

public class StringManager {

    public static final int ERROR_EMPTY_EMAIL_ADDRESS_VALIDATION = 0;
    public static final int ERROR_INVALID_EMAIL_ADDRESS = 1;
    public static final int ERROR_EMPTY_PASSWORD_VALIDATION = 2;
    public static final int ERROR_NOT_ENOUGH_PASSWORD_CHARS = 3;
    public static final int EMPTY_LIST_DESCRIPTION = 4;
    public static final int ERROR_NO_INTERNET = 5;

    private final Application application;

    @Inject
    public StringManager(Application application) {
        this.application = application;
    }

    public String getStringFromStringResource(int stringId) {
        if (stringId == ERROR_EMPTY_EMAIL_ADDRESS_VALIDATION) {
            return application.getString(R.string.emptyEmailAddressValidation);
        } else if (stringId == ERROR_INVALID_EMAIL_ADDRESS) {
            return application.getString(R.string.invalidEmailAddress);
        } else if (stringId == ERROR_EMPTY_PASSWORD_VALIDATION) {
            return application.getString(R.string.emptyPasswordValidation);
        } else if (stringId == ERROR_NOT_ENOUGH_PASSWORD_CHARS) {
            return application.getString(R.string.notEnoughPasswordChars);
        } else if (stringId == EMPTY_LIST_DESCRIPTION) {
            return application.getString(R.string.emptyListDescription);
        } else if (stringId == ERROR_NO_INTERNET) {
            return application.getString(R.string.emptyListDescription);
        } else {
            throw new RuntimeException("There is not such id.");
        }
    }

}
