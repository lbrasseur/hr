package com.aajtech.hr.service.api;

public interface LinkedInService {
	String buildLoginUrl();

	UserDto people(String accessToken);
}
