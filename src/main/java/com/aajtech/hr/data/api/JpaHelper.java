package com.aajtech.hr.data.api;

import javax.persistence.EntityManager;

public interface JpaHelper {
	interface JpaCallback<T> {
		T call(EntityManager entityManager) throws Exception;
	}

	<T> T doInJpa(JpaCallback<T> callback);
}
