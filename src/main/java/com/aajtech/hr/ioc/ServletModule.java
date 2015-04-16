package com.aajtech.hr.ioc;

import javax.inject.Singleton;

import com.aajtech.hr.ui.HrServlet;

public class ServletModule extends com.google.inject.servlet.ServletModule {
	@Override
	protected void configureServlets() {
		bind(HrServlet.class).in(Singleton.class);
		serve("/*").with(HrServlet.class);
	}
}