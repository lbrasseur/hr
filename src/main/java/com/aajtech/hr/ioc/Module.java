package com.aajtech.hr.ioc;

import java.io.IOException;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.aajtech.hr.business.api.PreferencesManager;
import com.aajtech.hr.business.api.TemplateManager;
import com.aajtech.hr.business.api.UserManager;
import com.aajtech.hr.business.impl.PreferencesManagerImpl;
import com.aajtech.hr.business.impl.TemplateManagerImpl;
import com.aajtech.hr.business.impl.UserManagerImpl;
import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.impl.JpaHelperImpl;
import com.aajtech.hr.ioc.Annotations.OAuth2RedirectUrl;
import com.aajtech.hr.ioc.Annotations.UserId;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.model.User;
import com.aajtech.hr.service.api.LinkedInService;
import com.aajtech.hr.service.impl.HttpClientLinkedInService;
import com.aajtech.hr.ui.HrUiProvider;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.extensions.appengine.datastore.AppEngineDataStoreFactory;
import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.servlet.SessionScoped;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.server.UIProvider;

public class Module extends AbstractModule {
	static final String PERSISTENCE_UNIT = "HR";

	@Override
	protected void configure() {
		requestStaticInjection(SerializableProvider.class);

		// UI
		bind(UIProvider.class).to(HrUiProvider.class).in(Singleton.class);
		bind(NavigationManager.class).in(SessionScoped.class);

		// Business
		bind(PreferencesManager.class).to(PreferencesManagerImpl.class);
		bind(TemplateManager.class).to(TemplateManagerImpl.class);
		bind(UserManager.class).to(UserManagerImpl.class);

		// Data
		bind(JpaHelper.class).to(JpaHelperImpl.class);

		// Services
		bind(HttpTransport.class).to(UrlFetchTransport.class);
		bind(JsonFactory.class).to(GsonFactory.class);
		bind(LinkedInService.class).to(HttpClientLinkedInService.class);
	}

	@Provides
	public EntityManager getEntityManager() {
		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT)
				.createEntityManager();
	}

	@Provides
	public JPAContainer<Template> getTemplateJPAContainerProvider(
			EntityManager enitityManager) {
		return JPAContainerFactory.make(Template.class, enitityManager);
	}

	@Provides
	public JPAContainer<User> getUserJPAContainerProvider(
			EntityManager enitityManager) {
		return JPAContainerFactory.make(User.class, enitityManager);
	}

	@Provides
	public AuthorizationCodeFlow getFlow(HttpTransport transport,
			JsonFactory jsonFactory) {
		try {
			String clientId = "7764mb7zvicxp7";
			String clientSecret = "XnKmdTVlr3qSWbGc";
			return new AuthorizationCodeFlow.Builder(
					BearerToken.authorizationHeaderAccessMethod(), transport,
					jsonFactory, new GenericUrl(
							"https://www.linkedin.com/uas/oauth2/accessToken"),
					new ClientParametersAuthentication(clientId, clientSecret),
					clientId,
					"https://www.linkedin.com/uas/oauth2/authorization")
					.setDataStoreFactory(
							AppEngineDataStoreFactory.getDefaultInstance())
					.build();
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	@Provides
	@UserId
	public String getUserId(HttpSession session) {
		return (String) session.getAttribute(UserId.KEY);
	}

	@Provides
	@OAuth2RedirectUrl
	public String getOAuth2RedirectUrl(HttpServletRequest request) {
		return request.getScheme()
				+ "://"
				+ request.getServerName()
				+ (request.getServerPort() != 80 ? ":"
						+ request.getServerPort() : "")
				+ ServletModule.OAUTH2_CALLBACK_PATH;
	}
}
