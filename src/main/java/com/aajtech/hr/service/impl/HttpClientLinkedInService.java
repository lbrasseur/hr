package com.aajtech.hr.service.impl;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.http.HttpSession;

import com.aajtech.hr.oauth.CallbackServlet;
import com.aajtech.hr.service.api.LinkedInService;
import com.aajtech.hr.service.api.UserDto;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Throwables;
import com.google.gson.Gson;

public class HttpClientLinkedInService implements LinkedInService {
	private final HttpTransport httpTransport;
	private final AuthorizationCodeFlow flow;
	private final Gson gson;
	private final Provider<HttpSession> sessionProvider;

	@Inject
	public HttpClientLinkedInService(HttpTransport httpTransport,
			AuthorizationCodeFlow flow, Gson gson,
			Provider<HttpSession> sessionProvider) {
		this.httpTransport = checkNotNull(httpTransport);
		this.flow = checkNotNull(flow);
		this.gson = checkNotNull(gson);
		this.sessionProvider = checkNotNull(sessionProvider);
	}

	@Override
	public String buildLoginUrl() {
		String state = UUID.randomUUID().toString();
		sessionProvider.get().setAttribute(CallbackServlet.STATE_KEY, state);
		AuthorizationCodeRequestUrl authorizeUrl = flow.newAuthorizationUrl()
				.setState(state)
				.setRedirectUri(CallbackServlet.REDIRECT_URI);

		return authorizeUrl.build();
	}

	@Override
	public UserDto people(String accessToken) {
		final Credential credential = new Credential(flow.getMethod());
		credential.setAccessToken(accessToken);
		return get("https://api.linkedin.com/v1/people/~?format=json",
				UserDto.class, credential);
	}

	private <T> T get(String url, Class<T> responseType, String userId) {
		try {
			return get(url, responseType, flow.loadCredential(userId));
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}

	private <T> T get(String url, Class<T> responseType, Credential credential) {
		try {
			HttpResponse httpResponse = httpTransport
					.createRequestFactory(credential)
					.buildGetRequest(new GenericUrl(url)).execute();
			return gson.fromJson(httpResponse.parseAsString(), responseType);
		} catch (IOException e) {
			throw Throwables.propagate(e);
		}
	}
}
