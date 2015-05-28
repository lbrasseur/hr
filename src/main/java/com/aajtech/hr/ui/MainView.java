package com.aajtech.hr.ui;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import javax.inject.Inject;

import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.oauth.CallbackServlet;
import com.aajtech.hr.ui.person.PersonView;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Link;

public class MainView extends BaseView {
	@Inject
	public MainView(final NavigationManager navigationManager,
			final SerializableProvider<PersonView> personViewProvider,
			AuthorizationCodeFlow flow) {
		requireNonNull(navigationManager);
		requireNonNull(personViewProvider);
		getNavigationBar().setCaption("Main Menu");
		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);
		
		
		
		container.addComponent(new Link("Login with LinkedIn", new ExternalResource(buildAuthorizationUrl(flow))));
		
//		HorizontalButtonGroup peopleRow = new HorizontalButtonGroup();
//		container.addComponent(peopleRow);
//		peopleRow.addComponent(new Label("People"));
//		peopleRow.addComponent(new Button("->", new ClickListener() {
//			@Override
//			public void buttonClick(ClickEvent event) {
//				goTo(personViewProvider.get());
//			}
//		}));
	}
	
    private String buildAuthorizationUrl(AuthorizationCodeFlow flow) {
        AuthorizationCodeRequestUrl authorizeUrl = flow
                .newAuthorizationUrl()
                .setState(UUID.randomUUID().toString())
                .setRedirectUri(CallbackServlet.REDIRECT_URI);

        return authorizeUrl.build();
    }
}
