package com.aajtech.hr.ui.template;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.inject.Inject;

import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.ui.BaseView;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;

public class TemplateView extends BaseView {
	private final JPAContainer<Template> templateContainer;

	@Inject
	public TemplateView(
			final SerializableProvider<TemplateForm> templateFormProvider,
			final SerializableProvider<TemplateUpload> templateUploadProvider,
			final JPAContainer<Template> templateContainer) {
		checkNotNull(templateFormProvider);
		this.templateContainer = checkNotNull(templateContainer);
		getNavigationBar().setCaption("Templates");

		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		container.addComponent(new HorizontalLayout(new Label("Template list"),
				new Button("Add", new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						TemplateForm form = templateFormProvider.get();
						form.edit(new Template());
						goTo(form);
					}
				})));

		Grid grid = new Grid();
		grid.setContainerDataSource(templateContainer);
		grid.removeAllColumns();
		grid.addColumn("name");
		grid.addItemClickListener(new ItemClickListener() {
			@Override
			public void itemClick(ItemClickEvent event) {
				@SuppressWarnings("unchecked")
				JPAContainerItem<Template> beanItem = (JPAContainerItem<Template>) event.getItem();
				TemplateUpload uppload = templateUploadProvider.get();
				uppload.edit(beanItem.getEntity());
				goTo(uppload);
			}
		});
		container.addComponent(grid);
	}

	@Override
	protected void onActivate() {
		templateContainer.refresh();
	}
}
