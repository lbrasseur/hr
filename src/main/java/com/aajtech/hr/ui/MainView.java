package com.aajtech.hr.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.inject.Inject;

import com.aajtech.hr.business.api.TemplateManager;
import com.aajtech.hr.business.api.UserManager;
import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.User;
import com.aajtech.hr.ui.template.TemplateView;
import com.aajtech.hr.ui.user.UserView;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;

public class MainView extends BaseView {
	@Inject
	public MainView(final NavigationManager navigationManager,
			final SerializableProvider<UserView> userViewProvider,
			final SerializableProvider<TemplateView> templateViewProvider,
			UserManager userManager,
			final SerializableProvider<TemplateManager> templateManagerProvider) {
		checkNotNull(navigationManager);
		checkNotNull(userViewProvider);
		checkNotNull(templateViewProvider);
		checkNotNull(userManager);
		checkNotNull(templateManagerProvider);

		getNavigationBar().setCaption("Main Menu");
		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		if (userManager.isUserLoggedIn()) {
			final User user = userManager.getUser();
			container.addComponent(new Label("Welcome " + user.getFirstName()
					+ " " + user.getLastName()));

			if (templateManagerProvider.get().isActiveTemplate()) {
				container.addComponent(new Link("Download Resume",
						new StreamResource(new StreamResource.StreamSource() {
							@Override
							public InputStream getStream() {
								return new ByteArrayInputStream(
										templateManagerProvider.get()
												.buildResume(user));
							}
						}, user.getFirstName() + " " + user.getLastName()
								+ ".docx")));
			}

			if (user.isAdmin()) {
				container.addComponent(buildMenuOption("Users",
						userViewProvider));
				container.addComponent(buildMenuOption("Templates",
						templateViewProvider));
			}
		} else {
			container.addComponent(new Link("Login with LinkedIn",
					new ExternalResource(userManager.buildLoginUrl())));
		}

	}

	private Component buildMenuOption(String title,
			final SerializableProvider<? extends BaseView> viewProvider) {
		HorizontalButtonGroup option = new HorizontalButtonGroup();
		option.addComponent(new Label(title));
		option.addComponent(new Button("->", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				goTo(viewProvider.get());
			}
		}));
		return option;
	}
}
