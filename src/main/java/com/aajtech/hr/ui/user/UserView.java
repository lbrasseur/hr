package com.aajtech.hr.ui.user;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.User;
import com.aajtech.hr.ui.BaseView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Grid;

public class UserView extends BaseView {
	private final JPAContainer<User> userContainer;

	@Inject
	public UserView(final SerializableProvider<UserForm> personFormProvider,
			final JPAContainer<User> userContainer) {
		checkNotNull(personFormProvider);
		this.userContainer = checkNotNull(userContainer);
		getNavigationBar().setCaption("Users");

		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		Grid grid = new Grid();
		grid.setContainerDataSource(userContainer);
		grid.removeAllColumns();
		grid.addColumn("firstName");
		grid.addColumn("lastName");
		grid.addColumn("email");
		grid.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				@SuppressWarnings("unchecked")
				JPAContainerItem<User> beanItem = (JPAContainerItem<User>) event.getItem();
				UserForm form = personFormProvider.get();
				form.edit(beanItem);
				goTo(form);
			}
		});
		container.addComponent(grid);
	}

	@Override
	protected void onActivate() {
		userContainer.refresh();
	}
}
