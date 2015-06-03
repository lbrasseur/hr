package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.aajtech.hr.business.api.UserManager;
import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.ioc.Annotations.UserId;
import com.aajtech.hr.model.Skill;
import com.aajtech.hr.model.User;
import com.aajtech.hr.model.UserSkill;
import com.aajtech.hr.service.api.LinkedInService;
import com.aajtech.hr.service.api.SkillIdDto;
import com.aajtech.hr.service.api.UserDto;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class UserManagerImpl implements UserManager {
	private final JpaHelper jpaHelper;
	private final LinkedInService linkedInService;
	private final Provider<String> userIdProvider;

	@Inject
	public UserManagerImpl(JpaHelper jpaHelper,
			LinkedInService linkedInService,
			@UserId Provider<String> userIdProvider) {
		this.jpaHelper = checkNotNull(jpaHelper);
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
		return jpaHelper.doInJpa(new JpaCallback<String>() {
			@Override
			public String call(EntityManager entityManager) {
				UserDto userDto = linkedInService.people(accessToken);
				User user = entityManager.find(User.class, userDto.getId());
				if (user == null) {
					user = new User(userDto.getId());
				}
				user.setFirstName(userDto.getFirstName());
				user.setLastName(userDto.getLastName());
				user.setHeadline(userDto.getHeadline());
				user.setSummary(userDto.getSummary());
				user.setEmail(userDto.getEmailAddress());
				user.setSpecialities(userDto.getSpecialties());

				if (userDto.getSkills() != null) {
					user.getSkills().clear();
					Iterable<String> skills = Iterables.transform(userDto
							.getSkills().getValues(),
							new Function<SkillIdDto, String>() {
								@Override
								public String apply(SkillIdDto skill) {
									return skill.getSkill().getName();
								}
							});
					for (String skillName : skills) {
						Skill skill = entityManager
								.find(Skill.class, skillName);
						if (skill == null) {
							skill = new Skill(skillName);
							entityManager.persist(skill);
						}
						user.getSkills().add(new UserSkill(user, skill));
					}
				}
				entityManager.merge(user);
				return userDto.getId();
			}
		});
	}

	@Override
	public User getUser() {
		return jpaHelper.doInJpa(new JpaCallback<User>() {
			@Override
			public User call(EntityManager entityManager) {
				return entityManager.find(User.class, userIdProvider.get());
			}
		});
	}
}
