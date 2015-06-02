package com.aajtech.hr.business.api;

import java.io.Serializable;

public interface PreferencesManager {
	String ACTIVE_TEMPLATE_KEY = "activeTemplate";

	<T extends Serializable> T getValue(String key);

	<T extends Serializable> void setValue(String key, T value);
}
