package com.aajtech.hr.service.api;

import com.google.common.collect.Iterables;

public class UserDto {
	private String id;
	private String firstName;
	private String lastName;
	private String headline;
	private String summary;
	private String emailAddress;
	private String specialties;
	private SkillsDto skills;
	private PositionsDto positions;

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getHeadline() {
		return headline;
	}

	public String getSummary() {
		return summary;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public String getSpecialties() {
		return specialties;
	}

	public SkillsDto getSkills() {
		return skills;
	}

	public Iterable<String> getSkillsNames() {
		return Iterables.transform(skills.getValues(), SkillIdDto.TO_NAME);
	}

	public PositionsDto getPositions() {
		return positions;
	}
}
