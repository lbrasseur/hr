package com.aajtech.hr.ui;

import com.vaadin.addon.touchkit.ui.TabBarView;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@Theme("touchkit")
@Widgetset("com.vaadin.addon.touchkit.gwt.TouchKitWidgetSet")
public class HrUi extends UI {
	private static final long serialVersionUID = -1151571549568162607L;

	@Override
	protected void init(VaadinRequest request) {
		TabBarView mainView = new TabBarView();
        setContent(mainView);
        mainView.addTab(new Label("Esto es un matanga"), "Matanga");
        mainView.addTab(new Label("Me quiero volver chango"), "Changos");
	}
}
