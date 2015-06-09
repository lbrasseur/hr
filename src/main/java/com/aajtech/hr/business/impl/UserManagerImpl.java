package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import com.aajtech.hr.business.api.UserManager;
import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.ioc.Annotations.UserId;
import com.aajtech.hr.model.Company;
import com.aajtech.hr.model.Position;
import com.aajtech.hr.model.Skill;
import com.aajtech.hr.model.User;
import com.aajtech.hr.model.UserSkill;
import com.aajtech.hr.service.api.CompanyDto;
import com.aajtech.hr.service.api.LinkedInService;
import com.aajtech.hr.service.api.PositionDto;
import com.aajtech.hr.service.api.UserDto;

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
		final UserDto userDto = linkedInService.people(accessToken);
		if (userDto.getSkills() != null) {
			for (final String skillName : userDto.getSkillsNames()) {
				jpaHelper.doInJpa(new JpaCallback<Void>() {
					@Override
					public Void call(EntityManager entityManager) {
						if (entityManager.find(Skill.class, skillName) == null) {
							entityManager.persist(new Skill(skillName));
						}
						return null;
					}
				});
			}
		}
		for (PositionDto positionDto : userDto.getPositions().getValues()) {
			final CompanyDto companyDto = positionDto.getCompany();
			jpaHelper.doInJpa(new JpaCallback<Void>() {
				@Override
				public Void call(EntityManager entityManager) {
					Company company = entityManager.find(Company.class,
							companyDto.getName());
					if (company == null) {
						company = new Company(companyDto.getName());
					}
					company.setIndustry(companyDto.getIndustry());
					company.setSize(companyDto.getSize());
					company.setType(companyDto.getType());
					entityManager.persist(company);
					return null;
				}
			});
		}
		return jpaHelper.doInJpa(new JpaCallback<String>() {
			@Override
			public String call(EntityManager entityManager) {
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
					for (String skillName : userDto.getSkillsNames()) {
						user.getSkills().add(new UserSkill(skillName));
					}
				}
				user.getPositions().clear();
				for (PositionDto positionDto : userDto.getPositions()
						.getValues()) {
					user.getPositions().add(
							new Position(positionDto.getCompany().getName(),
									positionDto.getStartDate(), positionDto
											.getEndDate(), positionDto
											.getTitle(), positionDto
											.getSummary()));
				}
				entityManager.persist(user);
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
