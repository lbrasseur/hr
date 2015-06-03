package com.aajtech.hr.service.api;

import java.util.Calendar;
import java.util.Date;

public class DateDto {
	private int month;
	private int year;

	public int getMonth() {
		return month;
	}

	public int getYear() {
		return year;
	}

	public Date toDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.DAY_OF_MONTH, 15);
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.YEAR, year);
		return calendar.getTime();
	}
}
