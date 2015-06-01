package com.aajtech.hr.ui.template;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.ui.BaseView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

public class TemplateForm extends BaseView {
	private final FormLayout form;
	private Template template;
	private FieldGroup binder;

	@Inject
	public TemplateForm(final SerializableProvider<JpaHelper> jpaHelperProvider) {
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
										entityManager.merge(template);
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

	public void edit(final Template template) {
		this.template = checkNotNull(template);
		BeanItem<Template> item = new BeanItem<Template>(template);

		binder = new BeanFieldGroup<Template>(Template.class);
		binder.setItemDataSource(item);
		Field<?> nameField = binder.buildAndBind("Name", "name");
		form.addComponent(nameField);
	}
}
