package com.niedzwiecki.przemyslguide.util;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by niedzwiedz on 17.08.17.
 */

public class Utils {

    public static final int TOAST_OFFSET = 150;

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(@Nullable final Collection<?> c) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(@Nullable final Map<?, ?> m) {
        return m == null || m.isEmpty();
    }

    private static final String EMAIL_PATTERN =
            "[A-Z0-9a-z._%+-]{2,}";

    public static boolean isValidEmail(CharSequence target) {
        return target != null && Pattern.compile(EMAIL_PATTERN).matcher(target).matches();
    }

    public static boolean isPasswordLengthMinimumFourChars(@Nullable CharSequence str) {
        return str != null && str.length() >= 4;
    }
}
