package com.niedzwiecki.przemyslguide.ui.base;

/**
 * Created by niedzwiedz on 29.06.17.
 */

class ApplicationController {

    private static ApplicationController instance;

    public static synchronized ApplicationController getInstance() {
        return instance;
    }

}
