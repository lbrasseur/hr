package com.aajtech.hr.service.api;

import java.util.Date;

public class PositionDto {
	private CompanyDto company;
	private DateDto startDate;
	private DateDto endDate;
	private String title;
	private String summary;

	public CompanyDto getCompany() {
		return company;
	}

	public Date getEndDate() {
		return toDate(endDate);
	}

	public Date getStartDate() {
		return toDate(startDate);
	}

	public String getTitle() {
		return title;
	}

	public String getSummary() {
		return summary;
	}

	private Date toDate(DateDto date) {
		return date != null ? date.toDate() : null;
	}
}
