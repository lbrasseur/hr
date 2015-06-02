package com.aajtech.hr.business.api;

import com.aajtech.hr.model.User;

public interface TemplateManager {
	boolean isActiveTemplate();

	void setActiveTemplate(String name);

	byte[] buildResume(User user);
}
