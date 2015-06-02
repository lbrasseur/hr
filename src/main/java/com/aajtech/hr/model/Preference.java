package com.aajtech.hr.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Preference {
	@Id
	private String key;

	@Column
	@Lob
	private Serializable value;

	public Preference() {
	}

	public Preference(String key, Serializable value) {
		this.key = checkNotNull(key);
		this.value = checkNotNull(value);
	}

	public String getKey() {
		return key;
	}

	public Serializable getValue() {
		return value;
	}

	public void setValue(Serializable value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(key, value);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Preference that = (Preference) o;
		return Objects.equals(key, that.key)
				&& Objects.equals(value, that.value);
	}
}
