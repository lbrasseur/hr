package com.aajtech.hr.ui;

import com.vaadin.addon.touchkit.ui.NavigationView;

public class BaseView extends NavigationView {
	protected void goTo(BaseView view) {
		onDeactivate();
		getNavigationManager().navigateTo(view);
		view.onActivate();
	}

	protected void back() {
		onDeactivate();
		getNavigationManager().navigateBack();
		BaseView current = (BaseView) getNavigationManager()
				.getCurrentComponent();
		current.onActivate();
	}

	protected void onActivate() {
	}

	protected void onDeactivate() {

	}
}
