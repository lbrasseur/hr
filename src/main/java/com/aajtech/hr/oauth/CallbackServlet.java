package com.aajtech.hr.oauth;

import static com.google.api.client.util.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.aajtech.hr.business.api.UserManager;
import com.aajtech.hr.ioc.Annotations.UserId;
import com.aajtech.hr.ioc.ServletModule;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.TokenResponse;

public class CallbackServlet extends HttpServlet {
	public static final String REDIRECT_URI = "http://localhost:8080"
			+ ServletModule.OAUTH2_CALLBACK_PATH;
	public static final String STATE_KEY = "oauthState";
	private final AuthorizationCodeFlow flow;
	private final UserManager userManager;

	@Inject
	public CallbackServlet(AuthorizationCodeFlow flow, UserManager userManager) {
		this.flow = checkNotNull(flow);
		this.userManager = checkNotNull(userManager);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		checkState(req.getParameter("state").equals(
				req.getSession().getAttribute(STATE_KEY)));
		req.getSession().removeAttribute(STATE_KEY);

		TokenResponse response = flow.newTokenRequest(req.getParameter("code"))
				.setRedirectUri(REDIRECT_URI).execute();

		String userId = userManager.updateUserData(response.getAccessToken());
		flow.createAndStoreCredential(response, userId);
		req.getSession().setAttribute(UserId.KEY, userId);
		resp.sendRedirect(ServletModule.UI_PATH);
	}
}
