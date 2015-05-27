package com.aajtech.hr.ui.person;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.Person;
import com.aajtech.hr.ui.BaseView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class PersonView extends BaseView {
	private final JPAContainer<Person> personContainer;
	@Inject
	public PersonView(final SerializableProvider<PersonForm> personFormProvider,
			final JPAContainer<Person> personContainer) {
		checkNotNull(personFormProvider);
		this.personContainer = checkNotNull(personContainer);
		getNavigationBar().setCaption("People");

		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		container.addComponent(new HorizontalLayout(new Label("People list"),
				new Button("Add", new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						PersonForm form = personFormProvider.get();
						form.edit(new Person());
						goTo(form);
					}
				})));

		Grid grid = new Grid();
		grid.setContainerDataSource(personContainer);
		grid.removeAllColumns();
		grid.addColumn("firstName");
		grid.addColumn("lastName");
		grid.addColumn("email");
		container.addComponent(grid);
	}

	@Override
	protected void onActivate() {
		personContainer.refresh();
	}
}
