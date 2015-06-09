package com.aajtech.hr.model;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.util.Date;
import java.util.Objects;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.google.appengine.api.datastore.Key;

@Entity
public class Position {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;
	@Column
	private String companyName;
	@Column
	private Date startDate;
	@Column
	private Date endDate;
	@Column
	private String title;
	@Column
	private String summary;

	public Position() {
	}

	public Position(String companyName, Date startDate,
			@Nullable Date endDate, String title, @Nullable String summary) {
		this.companyName = checkNotNull(companyName);
		this.startDate = checkNotNull(startDate);
		this.endDate = endDate;
		this.title = checkNotNull(title);
		this.summary = summary;
	}

	public String getCompanyName() {
		return companyName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, companyName, startDate, endDate, title, summary);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Position that = (Position) o;
		return Objects.equals(id, that.id)
				&& Objects.equals(companyName, that.companyName)
				&& Objects.equals(startDate, that.startDate)
				&& Objects.equals(endDate, that.endDate)
				&& Objects.equals(title, that.title)
				&& Objects.equals(summary, that.summary);
	}
}
