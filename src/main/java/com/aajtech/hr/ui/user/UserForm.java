package com.aajtech.hr.ui.user;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.User;
import com.aajtech.hr.ui.BaseView;
import com.google.common.base.Throwables;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

public class UserForm extends BaseView {
	private final FormLayout form;
	private JPAContainerItem<User> user;
	private FieldGroup binder;

	@Inject
	public UserForm(final SerializableProvider<EntityManager> emProvider) {
		checkNotNull(emProvider);
		getNavigationBar().setCaption("Edit template");

		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		container.addComponent(form = new FormLayout());

		container.addComponent(new HorizontalLayout(new Button("Save",
				new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						try {
							binder.commit();
							EntityManager em = emProvider.get();
							em.getTransaction().begin();
							em.merge(user.getEntity());
							em.getTransaction().commit();

							back();
						} catch (CommitException e) {
							Throwables.propagate(e);
						}
					}
				}), new Button("Cancel", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				back();
			}
		})));
	}

	public void edit(JPAContainerItem<User> user) {
		this.user = checkNotNull(user);

		binder = new BeanFieldGroup<User>(User.class);
		binder.setItemDataSource(user);
		form.addComponent(binder.buildAndBind("First name", "firstName"));
		form.addComponent(binder.buildAndBind("Last name", "lastName"));
		form.addComponent(binder.buildAndBind("Email", "email"));
	}
}