package com.aajtech.hr.model;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Skill {
	@Id
	private String name;

	public Skill() {
	}

	public Skill(String name) {
		this.name = checkNotNull(name);
	}

	public String getName() {
		return name;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Skill that = (Skill) o;
		return Objects.equals(name, that.name);
	}
}
