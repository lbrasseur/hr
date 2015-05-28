package com.aajtech.hr.oauth;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.io.Serializable;

public class User implements Serializable {
	private String id;
	private String firstName;
	private String lastName;

	public void copy(User other) {
		checkNotNull(other);
		id = other.id;
		firstName = other.firstName;
		lastName = other.lastName;
	}

	public String getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
}
