package com.aajtech.hr.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.aajtech.hr.business.api.UserManager;
import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.User;
import com.aajtech.hr.ui.person.PersonView;
import com.google.common.base.Preconditions;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;

public class MainView extends BaseView {
	@Inject
	public MainView(final NavigationManager navigationManager,
			final SerializableProvider<PersonView> personViewProvider,
			UserManager userManager) {
		checkNotNull(navigationManager);
		checkNotNull(personViewProvider);
		checkNotNull(userManager);

		getNavigationBar().setCaption("Main Menu");
		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		if (userManager.isUserLoggedIn()) {
			User user = userManager.getUser();
			container.addComponent(new Label("Welcome " + user.getFirstName()
					+ " " + user.getLastName()));
		} else {
			container.addComponent(new Link("Logn with LinkedIn",
					new ExternalResource(userManager.buildLoginUrl())));
		}

		HorizontalButtonGroup peopleRow = new HorizontalButtonGroup();
		container.addComponent(peopleRow);
		peopleRow.addComponent(new Label("People"));
		peopleRow.addComponent(new Button("->", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				goTo(personViewProvider.get());
			}
		}));
	}

}
