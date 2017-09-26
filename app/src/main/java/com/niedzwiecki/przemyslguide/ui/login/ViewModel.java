package com.niedzwiecki.przemyslguide.ui.login;


import com.niedzwiecki.przemyslguide.ui.base.Navigator;

/**
 * Created by Dawid N on 8/22/2016.
 */
public interface ViewModel<N extends Navigator> {

    void attachContext(N context);

    void detachContext();

}
