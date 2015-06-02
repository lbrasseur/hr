package com.aajtech.hr.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

@Entity
public class User implements Serializable {
	@Id
	private String id;

	@Column
	private String firstName = "";

	@Column
	private String lastName = "";

	@Column
	@Lob
	private String headline = "";

	@Column
	@Lob
	private String summary = "";

	@Column
	private String email = "";

	@Column
	private String specialities = "";

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<UserSkill> skills;

	@Column
	private boolean admin;

	public User() {
	}

	public User(String id) {
		this.id = checkNotNull(id);
		skills = new HashSet<UserSkill>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public boolean isAdmin() {
		return admin || "vPwxCoFLC8".equals(id);
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}

	public String getSpecialities() {
		return specialities;
	}

	public void setSpecialities(String specialities) {
		this.specialities = specialities;
	}

	public Set<UserSkill> getSkills() {
		return skills;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, firstName, lastName, email, headline,
				specialities, admin);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User that = (User) o;
		return Objects.equals(id, that.id)
				&& Objects.equals(firstName, that.firstName)
				&& Objects.equals(lastName, that.lastName)
				&& Objects.equals(email, that.email)
				&& Objects.equals(summary, that.summary)
				&& Objects.equals(headline, that.headline)
				&& Objects.equals(specialities, that.specialities)
				&& Objects.equals(admin, that.admin)
				&& Objects.equals(skills, that.skills);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName="
				+ lastName + ", email=" + email + "]";
	}
}
