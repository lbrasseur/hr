package com.aajtech.hr.ui;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.vaadin.addon.touchkit.server.TouchKitServlet;
import com.vaadin.server.ServiceException;
import com.vaadin.server.SessionInitEvent;
import com.vaadin.server.SessionInitListener;
import com.vaadin.server.UIProvider;

public class HrServlet extends TouchKitServlet implements
        SessionInitListener {
    private static final long serialVersionUID = 307114308024770755L;
    private final UIProvider uiProvider;

    @Inject
    public HrServlet(UIProvider uiProvider) {
        this.uiProvider = checkNotNull(uiProvider);
    }

    @Override
    protected void servletInitialized() {
        getService().addSessionInitListener(this);
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        event.getSession().addUIProvider(uiProvider);
    }
}
