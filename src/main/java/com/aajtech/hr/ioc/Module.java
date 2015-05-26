package com.aajtech.hr.ioc;

import javax.inject.Singleton;
import javax.persistence.EntityManager;

import com.aajtech.hr.model.Person;
import com.aajtech.hr.ui.HrUiProvider;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.TypeLiteral;
import com.google.inject.servlet.SessionScoped;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.server.UIProvider;

public class Module extends AbstractModule {
	static final String PERSISTENCE_UNIT = "HR";

	@Override
	protected void configure() {
		requestStaticInjection(SerializableProvider.class);

		// UI
		bind(UIProvider.class).to(HrUiProvider.class).in(Singleton.class);
		bind(NavigationManager.class).in(SessionScoped.class);

		// Business

		// Data
		bind(new TypeLiteral<JPAContainer<Person>>() {
		}).toProvider(new JPAContainerProvider<Person>(Person.class));

		// Services
	}

	@Provides
	public EntityManager getEntityManager() {
		return JPAContainerFactory
				.createEntityManagerForPersistenceUnit(PERSISTENCE_UNIT);
	}
}
