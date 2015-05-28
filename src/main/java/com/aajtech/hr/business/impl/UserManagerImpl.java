package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.aajtech.hr.business.api.UserManager;
import com.aajtech.hr.ioc.Annotations.UserId;
import com.aajtech.hr.model.User;
import com.aajtech.hr.service.api.LinkedInService;
import com.aajtech.hr.service.api.UserDto;

public class UserManagerImpl implements UserManager {
	private final LinkedInService linkedInService;
	private final Provider<String> userIdProvider;
	private final Provider<EntityManager> entityManagerProvider;

	@Inject
	public UserManagerImpl(LinkedInService linkedInService,
			@UserId Provider<String> userIdProvider,
			Provider<EntityManager> entityManagerProvider) {
		this.linkedInService = checkNotNull(linkedInService);
		this.userIdProvider = checkNotNull(userIdProvider);
		this.entityManagerProvider = checkNotNull(entityManagerProvider);
	}

	@Override
	public boolean isUserLoggedIn() {
		return userIdProvider.get() != null;
	}

	@Override
	public String buildLoginUrl() {
		return linkedInService.buildLoginUrl();
	}

	@Override
	public String updateUserData(String accessToken) {
		final UserDto userDto = linkedInService.people(accessToken);
		doInJpa(new JpaCallback<Void>() {

			@Override
			public Void call(EntityManager entityManager) {
				User user = entityManager.find(User.class, userDto.getId());
				if (user == null) {
					user = new User();
					user.setId(userDto.getId());
				}
				user.setFirstName(userDto.getFirstName());
				user.setLastName(userDto.getLastName());
				user.setHeadline(userDto.getHeadline());
				entityManager.persist(user);
				return null;
			}
		});
		return userDto.getId();
	}

	@Override
	public User getUser() {
		return doInJpa(new JpaCallback<User>() {
			@Override
			public User call(EntityManager entityManager) {
				return entityManager.find(User.class, userIdProvider.get());
			}
		});
	}

	private <T> T doInJpa(JpaCallback<T> callback) {
		EntityManager entityManager = entityManagerProvider.get();
		entityManager.getTransaction().begin();
		T returnValue = callback.call(entityManager);
		entityManager.getTransaction().commit();
		entityManager.close();
		return returnValue;
	}

	private interface JpaCallback<T> {
		T call(EntityManager entityManager);
	}
}
