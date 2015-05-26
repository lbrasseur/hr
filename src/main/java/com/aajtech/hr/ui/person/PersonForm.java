package com.aajtech.hr.ui.person;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.Person;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;

public class PersonForm extends NavigationView {
	private final FormLayout form;
	private Person person;

	@Inject
	public PersonForm(final SerializableProvider<EntityManager> emProvider) {
		getNavigationBar().setCaption("Edit person");

		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		container.addComponent(form = new FormLayout());

		container.addComponent(new HorizontalLayout(new Button("Save",
				new ClickListener() {

					@Override
					public void buttonClick(ClickEvent event) {
						EntityManager em = emProvider.get();
						em.getTransaction().begin();
						em.persist(person);
						em.getTransaction().commit();

						getNavigationManager().navigateBack();
					}
				}), new Button("Cancel", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				getNavigationManager().navigateBack();
			}
		})));
	}

	public void edit(Person person) {
		this.person = checkNotNull(person);
		BeanItem<Person> item = new BeanItem<Person>(person);

		FieldGroup binder = new FieldGroup(item);
		form.addComponent(binder.buildAndBind("First name", "firstName"));
		form.addComponent(binder.buildAndBind("Last name", "lastName"));
		form.addComponent(binder.buildAndBind("Email", "email"));
	}
}
