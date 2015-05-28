package com.aajtech.hr.oauth;

import static com.google.api.client.util.Preconditions.checkNotNull;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.gson.Gson;

public class CallbackServlet extends HttpServlet {
	public static final String PATH = "/oauth2callback";
	public static final String REDIRECT_URI = "http://localhost:8080" + PATH;
	private final AuthorizationCodeFlow flow;
	private final Provider<User> userProvider;
	private final HttpTransport httpTransport;
	private final JsonFactory jsonFactory;

	@Inject
	public CallbackServlet(AuthorizationCodeFlow flow,
			Provider<User> userProvider, HttpTransport httpTransport,
			JsonFactory jsonFactory) {
		this.flow = checkNotNull(flow);
		this.userProvider = checkNotNull(userProvider);
		this.httpTransport = checkNotNull(httpTransport);
		this.jsonFactory = checkNotNull(jsonFactory);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		TokenResponse response = flow.newTokenRequest(req.getParameter("code"))
				.setRedirectUri(REDIRECT_URI).execute();

		final Credential credential = new Credential(flow.getMethod());
		credential.setAccessToken(response.getAccessToken());

		HttpResponse httpResponse = httpTransport
				.createRequestFactory(new HttpRequestInitializer() {
		            @Override
		            public void initialize(HttpRequest request) throws IOException {
		            	credential.initialize(request);
		              request.setParser(new JsonObjectParser(jsonFactory));
		            }
		          }
						)
				.buildGetRequest(
						new GenericUrl(
								"https://api.linkedin.com/v1/people/~?format=json"))
				.execute();
		User user = new Gson().fromJson(httpResponse.parseAsString(), User.class);

		
		flow.createAndStoreCredential(response, user.getId());

		userProvider.get().copy(user);

		resp.sendRedirect("/");
	}
}
