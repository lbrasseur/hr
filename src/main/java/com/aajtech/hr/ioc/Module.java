package com.aajtech.hr.ioc;

import javax.inject.Singleton;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.aajtech.hr.ui.HrUiProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.vaadin.server.UIProvider;

public class Module extends AbstractModule {
    @Override
    protected void configure() {
        // UI
        bind(UIProvider.class).to(HrUiProvider.class);

        // Business
        
        // Services
    }

    @Provides
    @Singleton
    public EntityManagerFactory getEntityManagerFactory() {
        return Persistence.createEntityManagerFactory("Hr");
    }
}
