package com.aajtech.hr.business.api;

import com.aajtech.hr.model.User;

public interface TemplateManager {
	byte[] buildResume(User user);
}
