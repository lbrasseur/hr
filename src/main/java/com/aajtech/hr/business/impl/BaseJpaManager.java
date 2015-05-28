package com.aajtech.hr.business.impl;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.google.common.base.Throwables;

class BaseJpaManager {
	private final Provider<EntityManager> entityManagerProvider;

	BaseJpaManager(Provider<EntityManager> entityManagerProvider) {
		this.entityManagerProvider = entityManagerProvider;
	}

	<T> T doInJpa(JpaCallback<T> callback) {
		EntityManager entityManager = entityManagerProvider.get();
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			T returnValue = callback.call(entityManager);
			tx.commit();
			return returnValue;
		} catch (Exception e) {
			tx.rollback();
			throw Throwables.propagate(e);
		} finally {
			entityManager.close();
		}
	}

	interface JpaCallback<T> {
		T call(EntityManager entityManager) throws Exception;
	}
}
