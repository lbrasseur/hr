package com.aajtech.hr.ui.template;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.ui.BaseView;
import com.google.common.base.Strings;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItem;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Link;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class TemplateForm extends BaseView {
	private final FormLayout form;
	private Template template;
	private FieldGroup binder;
	private final SerializableProvider<JpaHelper> jpaHelperProvider;

	@Inject
	public TemplateForm(final SerializableProvider<JpaHelper> jpaHelperProvider) {
		this.jpaHelperProvider = checkNotNull(jpaHelperProvider);
		getNavigationBar().setCaption("Edit user");

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
										entityManager.merge(template);
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
		if (!Strings.isNullOrEmpty(template.getName())) {
			nameField.setReadOnly(true);
		}
		form.addComponent(nameField);
		TemplateUploader uploader = new TemplateUploader();
		Upload upload = new Upload("Template", uploader);
		upload.addSucceededListener(uploader);
		form.addComponent(upload);
		form.addComponent(new Link("Download", new StreamResource(
				new StreamResource.StreamSource() {
					@Override
					public InputStream getStream() {
						return new ByteArrayInputStream(template.getFile());
					}
				}, template.getName() + ".docx")));
	}

	private class TemplateUploader implements Receiver, SucceededListener,
			Serializable {
		public transient ByteArrayOutputStream buffer;

		public OutputStream receiveUpload(String filename, String mimeType) {
			if (buffer == null) {
				buffer = new ByteArrayOutputStream();
			}
			return buffer;
		}

		public void uploadSucceeded(SucceededEvent event) {
			template.setFile(buffer.toByteArray());
			jpaHelperProvider.get().doInJpa(new JpaCallback<Void>() {
				@Override
				public Void call(EntityManager entityManager) throws Exception {
					entityManager.merge(template);
					return null;
				}
			});
		}
	}
}
