package com.aajtech.hr.ioc;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Provider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;

public class JPAContainerProvider<T> implements Provider<JPAContainer<T>> {
	private final Class<T> entityClass;

	public JPAContainerProvider(Class<T> entityClass) {
		this.entityClass = checkNotNull(entityClass);
	}

	@Override
	public JPAContainer<T> get() {
		return JPAContainerFactory.make(entityClass, Module.PERSISTENCE_UNIT);
	}
}
