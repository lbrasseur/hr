package com.aajtech.hr.ui.user;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.User;
import com.aajtech.hr.ui.BaseView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

public class UserForm extends BaseView {
	private final FormLayout form;
	private User user;
	private FieldGroup binder;

	@Inject
	public UserForm(final SerializableProvider<EntityManager> emProvider,
			final SerializableProvider<JpaHelper> jpaHelperProvider) {
		checkNotNull(emProvider);
		checkNotNull(jpaHelperProvider);
		getNavigationBar().setCaption("Edit template");

		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		container.addComponent(form = new FormLayout());

		container.addComponent(new HorizontalLayout(new Button("Save",
				new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						jpaHelperProvider.get().doInJpa(
								new JpaCallback<Void>() {
									@Override
									public Void call(EntityManager entityManager)
											throws Exception {
										binder.commit();
										entityManager.merge(user);

										back();
										return null;
									}
								});
					}
				}), new Button("Cancel", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				back();
			}
		})));
	}

	public void edit(User user) {
		this.user = checkNotNull(user);
		BeanItem<User> item = new BeanItem<User>(user);

		binder = new BeanFieldGroup<User>(User.class);
		binder.setItemDataSource(item);
		form.addComponent(binder.buildAndBind("First name", "firstName"));
		form.addComponent(binder.buildAndBind("Last name", "lastName"));
		form.addComponent(binder.buildAndBind("Email", "email"));
	}
}
