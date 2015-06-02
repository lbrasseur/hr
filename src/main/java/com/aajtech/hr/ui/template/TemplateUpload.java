package com.aajtech.hr.ui.template;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.aajtech.hr.business.api.TemplateManager;
import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.ioc.SerializableProvider;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.ui.BaseView;
import com.vaadin.addon.touchkit.ui.VerticalComponentGroup;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Link;
import com.vaadin.ui.Upload;
import com.vaadin.ui.Upload.Receiver;
import com.vaadin.ui.Upload.SucceededEvent;
import com.vaadin.ui.Upload.SucceededListener;

public class TemplateUpload extends BaseView {
	private Template template;
	private final SerializableProvider<JpaHelper> jpaHelperProvider;
	private final Link download;

	@Inject
	public TemplateUpload(
			final SerializableProvider<JpaHelper> jpaHelperProvider,
			final SerializableProvider<TemplateManager> templateManagerProvider) {
		this.jpaHelperProvider = checkNotNull(jpaHelperProvider);
		checkNotNull(templateManagerProvider);
		getNavigationBar().setCaption("Edit template");

		VerticalComponentGroup container = new VerticalComponentGroup();
		setContent(container);

		TemplateUploader uploader = new TemplateUploader();
		Upload upload = new Upload("Template", uploader);
		upload.addSucceededListener(uploader);

		container.addComponent(upload);

		container.addComponent(download = new Link());
		download.setCaption("Download");
		container.addComponent(new Button("Set as active", new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				templateManagerProvider.get().setActiveTemplate(
						template.getName());
				back();
			}
		}));
	}

	public void edit(final Template template) {
		this.template = checkNotNull(template);
		download.setResource(new StreamResource(
				new StreamResource.StreamSource() {
					@Override
					public InputStream getStream() {
						return new ByteArrayInputStream(template.getFile());
					}
				}, template.getName() + ".docx"));
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
			back();
		}
	}
}
