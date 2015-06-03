package com.aajtech.hr.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class UserSkill {
	@Id
	@ManyToOne
	private User user;
	@Id
	@ManyToOne
	private Skill skill;

	public UserSkill() {
	}

	public UserSkill(User user, Skill skill) {
		this.user = checkNotNull(user);
		this.skill = checkNotNull(skill);
	}

	public Skill getSkill() {
		return skill;
	}
}
