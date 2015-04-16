package com.aajtech.hr.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.inject.Provider;

import com.vaadin.server.UIClassSelectionEvent;
import com.vaadin.server.UICreateEvent;
import com.vaadin.server.UIProvider;
import com.vaadin.ui.UI;

public class HrUiProvider extends UIProvider {
	private static final long serialVersionUID = -8760571989746581405L;
	private final Provider<HrUi> provider;

	@Inject
	public HrUiProvider(Provider<HrUi> provider) {
		this.provider = checkNotNull(provider);
	}

	@Override
	public UI createInstance(UICreateEvent event) {
		return provider.get();
	}

	@Override
	public Class<? extends UI> getUIClass(UIClassSelectionEvent event) {
		return HrUi.class;
	}
}
