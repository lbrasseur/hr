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

public class UserManagerImpl extends BaseJpaManager implements UserManager {
	private final LinkedInService linkedInService;
	private final Provider<String> userIdProvider;

	@Inject
	public UserManagerImpl(LinkedInService linkedInService,
			@UserId Provider<String> userIdProvider,
			Provider<EntityManager> entityManagerProvider) {
		super(entityManagerProvider);
		this.linkedInService = checkNotNull(linkedInService);
		this.userIdProvider = checkNotNull(userIdProvider);
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
	public String updateUserData(final String accessToken) {
		return doInJpa(new JpaCallback<String>() {
			@Override
			public String call(EntityManager entityManager) {
				UserDto userDto = linkedInService.people(accessToken);
				User user = entityManager.find(User.class, userDto.getId());
				if (user == null) {
					user = new User();
					user.setId(userDto.getId());
				}
				user.setFirstName(userDto.getFirstName());
				user.setLastName(userDto.getLastName());
				user.setHeadline(userDto.getHeadline());
				user.setSummary(userDto.getSummary());
				user.setEmail(userDto.getEmailAddress());
				entityManager.persist(user);
				return userDto.getId();
			}
		});
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
}
