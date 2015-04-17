package com.aajtech.hr.ui.person;

import com.aajtech.hr.model.Person;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.touchkit.ui.NavigationView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.ui.ListSelect;

public class PersonView extends NavigationView {
	public PersonView() {
		getNavigationBar().setCaption("People");
		
		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);
		ListSelect list = new ListSelect();
		list.setContainerDataSource(JPAContainerFactory.make(Person.class, "HR"));
		container.addComponent(list);
	}
}
