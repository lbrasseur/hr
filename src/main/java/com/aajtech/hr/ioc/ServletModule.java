package com.aajtech.hr.ioc;

import javax.inject.Singleton;
import javax.servlet.http.HttpServlet;

import com.aajtech.hr.oauth.CallbackServlet;
import com.aajtech.hr.ui.HrGaeServlet;
import com.aajtech.hr.ui.HrServlet;

public class ServletModule extends com.google.inject.servlet.ServletModule {
	public static final String UI_PATH = "/ui";
	public static final String OAUTH2_CALLBACK_PATH = "/oauth2callback";

	@Override
	protected void configureServlets() {
		Class<? extends HttpServlet> servletClass = HrGaeServlet.inGae()
				&& HrGaeServlet.inGaeProd() ? HrGaeServlet.class
				: HrServlet.class;

		bind(servletClass).in(Singleton.class);
		serve(UI_PATH, UI_PATH + "/*", "/VAADIN/*", "/UIDL/*").with(
				servletClass);

		bind(CallbackServlet.class).in(Singleton.class);
		serve(OAUTH2_CALLBACK_PATH).with(CallbackServlet.class);
	}
}