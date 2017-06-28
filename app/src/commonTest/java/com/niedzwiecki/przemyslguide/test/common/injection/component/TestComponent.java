package com.niedzwiecki.przemyslguide.test.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import com.niedzwiecki.przemyslguide.injection.component.ApplicationComponent;
import com.niedzwiecki.przemyslguide.test.common.injection.module.ApplicationTestModule;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends ApplicationComponent {

}
