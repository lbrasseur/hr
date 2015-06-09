package com.aajtech.hr.model;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class UserSkill {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;
	@Column
	private String skillName;

	public UserSkill() {
	}

	public UserSkill(String skillName) {
		this.skillName = checkNotNull(skillName);
	}

	public String getSkillName() {
		return skillName;
	}
}
