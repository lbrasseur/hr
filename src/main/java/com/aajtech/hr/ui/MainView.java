package com.aajtech.hr.ui;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;

import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.ui.person.PersonView;
import com.vaadin.addon.touchkit.ui.HorizontalButtonGroup;
import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;

public class MainView extends BaseView {
	@Inject
	public MainView(final NavigationManager navigationManager,
			final SerializableProvider<PersonView> personViewProvider) {
		requireNonNull(navigationManager);
		requireNonNull(personViewProvider);
		getNavigationBar().setCaption("Main Menu");
		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);
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
