package com.aajtech.hr.model;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

@Entity
public class Template implements Serializable {
	@Id
	private String name = "";
	@Lob
	private byte[] file;

	public Template() {
	}

	public Template(String name) {
		this.name = checkNotNull(name);
	}

	public String getName() {
		return name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, file);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Template that = (Template) o;
		return Objects.equals(name, that.name)
				&& Objects.equals(file, that.file);
	}
}
