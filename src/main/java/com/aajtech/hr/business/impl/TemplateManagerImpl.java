package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

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
		findAndReplace(runs, "[firstName]", user.getFirstName());
		findAndReplace(runs, "[lastName]", user.getLastName());
		findAndReplace(runs, "[headline]", user.getHeadline());
		findAndReplace(runs, "[summary]", user.getSummary());
		findAndReplace(runs, "[specialities]", user.getSpecialities());
	}

	private void findAndReplace(List<XWPFRun> runs, String template,
			String value) {
		int startRun = 0;
		int startChar = 0;
		while (startRun < runs.size()) {
			int endRun = startRun;
			int endChar = startChar;
			int templateChar = 0;

			boolean found = false;
			while (endRun < runs.size()
					&& endChar < runs.get(endRun).getText(0).length()) {
				XWPFRun run = runs.get(endRun);
				String runText = run.getText(0);
				if (runText.charAt(endChar) == template.charAt(templateChar)) {
					templateChar++;
					endChar++;
					if (templateChar == template.length()) {
						found = true;
						break;
					} else if (endChar == runText.length()) {
						endRun++;
						endChar = 0;
					}
				} else {
					break;
				}
			}
			if (found) {
				replace(runs, startRun, startChar, endRun, endChar, value);
			}
			startRun = endRun;
			startChar = endChar + 1;
			if (startChar > runs.get(startRun).getText(0).length()) {
				startRun++;
				startChar = 0;
			}
		}
	}

	private void replace(List<XWPFRun> runs, int startRunPos, int startChar,
			int endRunPos, int endChar, String value) {
		XWPFRun startRun = runs.get(startRunPos);
		String startRunText = startRun.getText(0);
		if (startRunPos == endRunPos) {
			startRun.setText(startRunText.substring(0, startChar) + value
					+ startRunText.substring(endChar), 0);
		} else {
			XWPFRun endRun = runs.get(endRunPos);
			String endRunText = endRun.getText(0);

			startRun.setText(startRunText.substring(0, startChar) + value, 0);
			endRun.setText(endRunText.substring(endChar), 0);

			for (int n = startRunPos + 1; n < endRunPos; n++) {
				runs.get(n).setText("", 0);
			}
		}
	}
}
