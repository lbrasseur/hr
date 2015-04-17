package com.aajtech.hr.ui;

import static java.util.Objects.requireNonNull;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.ui.NavigationManager;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;

@Theme("touchkit")
@Widgetset("com.vaadin.addon.touchkit.gwt.TouchKitWidgetSet")
public class HrUi extends UI {
	private final NavigationManager navigationManager;
	private final MainView mainView;

	@Inject
	public HrUi(NavigationManager navigationManager, MainView mainView) {
		this.navigationManager = requireNonNull(navigationManager);
		this.mainView = requireNonNull(mainView);
	}

	@Override
	protected void init(VaadinRequest request) {
		setContent(navigationManager);
		navigationManager.setCurrentComponent(mainView);
	}
}
