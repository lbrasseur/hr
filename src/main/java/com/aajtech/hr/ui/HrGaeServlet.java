package com.aajtech.hr.ui;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;
import javax.inject.Provider;

import com.google.appengine.api.utils.SystemProperty;
import com.vaadin.server.GAEVaadinServlet;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIProvider;

public class HrGaeServlet extends GAEVaadinServlet implements
		SessionInitListener {
	private final UIProvider uiProvider;

	public static boolean inGae() {
		return System.getProperty("com.google.appengine.runtime.version") != null;
	}

	public static boolean inGaeProd() {
		return SystemProperty.environment.value() == SystemProperty.Environment.Value.Production;
	}

	@Inject
	public HrGaeServlet(UIProvider uiProvider) {
		this.uiProvider = requireNonNull(uiProvider);
	}

	@Override
	protected void servletInitialized() {
		getService().addSessionInitListener(this);
	}

	@Override
	public void sessionInit(SessionInitEvent event) throws ServiceException {
		event.getSession().addUIProvider(uiProvider);
	}
}
