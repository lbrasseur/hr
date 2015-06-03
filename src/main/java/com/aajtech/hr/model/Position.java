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
import javax.persistence.ManyToOne;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@Entity
public class Position {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key id;
	@ManyToOne
	private User user;
	@ManyToOne
	@Unowned
	private Company company;
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

	public Position(User user, Company company, Date startDate,
			@Nullable Date endDate, String title, String summary) {
		this.user = checkNotNull(user);
		this.company = checkNotNull(company);
		this.startDate = checkNotNull(startDate);
		this.endDate = endDate;
		this.title = checkNotNull(title);
		this.summary = checkNotNull(summary);
	}

	public Company getCompany() {
		return company;
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
		return Objects.hash(id, company, startDate, endDate, title, summary);
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
				&& Objects.equals(company, that.company)
				&& Objects.equals(startDate, that.startDate)
				&& Objects.equals(endDate, that.endDate)
				&& Objects.equals(title, that.title)
				&& Objects.equals(summary, that.summary);
	}
}
