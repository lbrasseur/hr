package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.aajtech.hr.business.api.PreferencesManager;
import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.model.Preference;

public class PreferencesManagerImpl implements PreferencesManager {
	private final JpaHelper jpaHelper;

	@Inject
	public PreferencesManagerImpl(JpaHelper jpaHelper) {
		this.jpaHelper = checkNotNull(jpaHelper);
	}

	@Override
	public <T extends Serializable> T getValue(final String key) {
		return jpaHelper.doInJpa(new JpaCallback<T>() {
			@SuppressWarnings("unchecked")
			@Override
			public T call(EntityManager entityManager) {
				Preference preference = entityManager.find(Preference.class,
						key);
				return preference != null ? (T) preference.getValue() : null;
			}
		});
	}

	@Override
	public <T extends Serializable> void setValue(final String key,
			final T value) {
		jpaHelper.doInJpa(new JpaCallback<Void>() {
			@Override
			public Void call(EntityManager entityManager) {
				Preference preference = entityManager.find(Preference.class,
						key);
				if (preference != null) {
					preference.setValue(value);
					entityManager.merge(preference);
				} else {
					preference = new Preference();
					preference.setKey(key);
					preference.setValue(value);
					entityManager.persist(preference);
				}
				return null;
			}
		});
	}
}
