package com.aajtech.hr.model;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Company {
	@Id
	private String name;
	@Column
	private String industry;
	@Column
	private String size;
	@Column
	private String type;

	public Company() {
	}

	public Company(String name) {
		this.name = checkNotNull(name);
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIndustry() {
		return industry;
	}

	public String getName() {
		return name;
	}

	public String getSize() {
		return size;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, industry, size, type);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Company that = (Company) o;
		return Objects.equals(name, that.name)
				&& Objects.equals(industry, that.industry)
				&& Objects.equals(size, that.size)
				&& Objects.equals(type, that.type);
	}
}
