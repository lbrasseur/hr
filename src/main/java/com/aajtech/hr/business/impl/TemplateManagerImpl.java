package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import groovy.lang.GroovyShell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.aajtech.hr.business.api.PreferencesManager;
import com.aajtech.hr.business.api.TemplateManager;
import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.model.User;
import com.google.common.base.Optional;
import com.google.common.base.Strings;

public class TemplateManagerImpl implements TemplateManager {
	private final JpaHelper jpaHelper;
	private final PreferencesManager preferencesManager;

	@Inject
	public TemplateManagerImpl(JpaHelper jpaHelper,
			PreferencesManager preferencesManager) {
		this.jpaHelper = checkNotNull(jpaHelper);
		this.preferencesManager = checkNotNull(preferencesManager);
	}

	@Override
	public boolean isActiveTemplate() {
		return jpaHelper.doInJpa(new JpaCallback<Boolean>() {
			@Override
			public Boolean call(EntityManager entityManager) throws IOException {
				return preferencesManager
						.getValue(PreferencesManager.ACTIVE_TEMPLATE_KEY) != null;
			}
		});
	}

	@Override
	public void setActiveTemplate(final String name) {
		jpaHelper.doInJpa(new JpaCallback<Void>() {
			@Override
			public Void call(EntityManager entityManager) throws IOException {
				preferencesManager.setValue(
						PreferencesManager.ACTIVE_TEMPLATE_KEY, name);
				return null;
			}
		});
	}

	@Override
	public byte[] buildResume(final User user) {
		return jpaHelper.doInJpa(new JpaCallback<byte[]>() {
			@Override
			public byte[] call(EntityManager entityManager) throws IOException {
				Template template = entityManager.find(
						Template.class,
						preferencesManager
								.getValue(PreferencesManager.ACTIVE_TEMPLATE_KEY));

				checkState(template != null, "There is no active template");

				XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(
						template.getFile()));
				replaceUserData(doc, user);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				doc.write(baos);
				return baos.toByteArray();
			}
		});
	}

	private void replaceUserData(XWPFDocument doc, User user) {
		GroovyShell groovyShell = new GroovyShell();
		groovyShell.setVariable("user", user);

		DocPointer start = new DocPointer(doc, 0, 0, 0);
		// Paragrpah 0 is not valid
		while (!start.isValid()) {
			start.increase();
		}
		while (start.isValid()) {
			while (start.isValid() && start.getChar() != '{') {
				start.increase();
			}
			if (start.isValid()) {
				DocPointer end = start.clone();
				int nestCount = 1;
				while (end.isValid() && nestCount > 0) {
					end.increase();
					if (end.isValid()) {
						if (end.getChar() == '{') {
							nestCount++;
						} else if (end.getChar() == '}') {
							nestCount--;
						}
					}
				}
				if (end.isValid()) {
					Content content = new Content(doc);
					groovyShell.setVariable("content", content);
					String scriptText = start.clone().increase().getString(end)
							.replaceAll("“", "\"").replaceAll("”", "\"")
							.replaceAll("‘", "\'").replaceAll("’", "\'");
					String value = Optional
							.of(groovyShell.evaluate(scriptText)).or("")
							.toString();
					if (!Strings.isNullOrEmpty(value)) {
						content.add(value);
					}
					start.replace(end.clone().increase(), content.toString());
					start = end.increase();
				} else {
					break;
				}
			}
		}
	}
}
