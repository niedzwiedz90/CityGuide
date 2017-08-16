package com.niedzwiecki.przemyslguide.util;

import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * Created by niedzwiedz on 17.08.17.
 */

public class Utils {

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    public static boolean isEmpty(@Nullable final Collection< ? > c ) {
        return c == null || c.isEmpty();
    }

    public static boolean isEmpty(@Nullable final Map< ?, ? > m ) {
        return m == null || m.isEmpty();
    }

}
