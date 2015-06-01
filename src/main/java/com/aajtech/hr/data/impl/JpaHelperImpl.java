package com.aajtech.hr.data.impl;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import com.aajtech.hr.data.api.JpaHelper;
import com.google.common.base.Throwables;

public class JpaHelperImpl implements JpaHelper {
	private final Provider<EntityManager> entityManagerProvider;

	@Inject
	public JpaHelperImpl(Provider<EntityManager> entityManagerProvider) {
		this.entityManagerProvider = entityManagerProvider;
	}

	@Override
	public <T> T doInJpa(JpaCallback<T> callback) {
		EntityManager entityManager = entityManagerProvider.get();
		EntityTransaction tx = entityManager.getTransaction();
		try {
			tx.begin();
			T returnValue = callback.call(entityManager);
			entityManager.flush();
			tx.commit();
			return returnValue;
		} catch (Exception e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw Throwables.propagate(e);
		} finally {
			entityManager.close();
		}
	}
}
