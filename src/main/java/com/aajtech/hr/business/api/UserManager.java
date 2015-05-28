package com.aajtech.hr.business.api;

import com.aajtech.hr.model.User;

public interface UserManager {
	boolean isUserLoggedIn();

	String buildLoginUrl();

	String updateUserData(String accessToken);
	
	User getUser();
}
