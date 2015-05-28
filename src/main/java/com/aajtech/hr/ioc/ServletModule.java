package com.aajtech.hr.ioc;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;

import com.aajtech.hr.oauth.CallbackServlet;
import com.aajtech.hr.ui.HrGaeServlet;
import com.aajtech.hr.ui.HrServlet;

public class ServletModule extends com.google.inject.servlet.ServletModule {
	@Override
	protected void configureServlets() {
		Class<? extends HttpServlet> servletClass = HrGaeServlet.inGae() && HrGaeServlet.inGaeProd()
				? HrGaeServlet.class
				: HrServlet.class;

		bind(servletClass).in(Singleton.class);
		serve("/").with(servletClass);
		serve("/VAADIN/*").with(servletClass);
		
		bind(CallbackServlet.class).in(Singleton.class);
		serve(CallbackServlet.PATH).with(CallbackServlet.class);
	}
}