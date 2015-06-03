package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import groovy.lang.GroovyShell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.aajtech.hr.business.api.PreferencesManager;
import com.aajtech.hr.business.api.TemplateManager;
import com.aajtech.hr.data.api.JpaHelper;
import com.aajtech.hr.data.api.JpaHelper.JpaCallback;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.model.User;

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

				for (XWPFParagraph p : doc.getParagraphs()) {
					List<XWPFRun> runs = p.getRuns();
					if (runs != null) {
						replaceUserData(runs, user);
					}
				}
				for (XWPFTable tbl : doc.getTables()) {
					for (XWPFTableRow row : tbl.getRows()) {
						for (XWPFTableCell cell : row.getTableCells()) {
							for (XWPFParagraph p : cell.getParagraphs()) {
								replaceUserData(p.getRuns(), user);
							}
						}
					}
				}

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				doc.write(baos);
				return baos.toByteArray();
			}
		});
	}

	private void replaceUserData(List<XWPFRun> runs, User user) {
		GroovyShell groovyShell = new GroovyShell();
		groovyShell.setVariable("user", user);

		RunPointer start = new RunPointer(runs);
		while (start.isValid()) {
			while (start.isValid() && start.getChar() != '{') {
				start.increase();
			}
			if (start.isValid()) {
				RunPointer end = start.clone();
				end.increase();
				while (end.isValid() && end.getChar() != '}') {
					end.increase();
				}
				if (end.isValid()) {
					String scriptText = start.clone().increase().getString(end);
					String value = groovyShell.evaluate(scriptText).toString();
					start.replace(end.clone().increase(), value);
					start = end.increase();
				} else{
					break;
				}
			}
		}
	}
}
