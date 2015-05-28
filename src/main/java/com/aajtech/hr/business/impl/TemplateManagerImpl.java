package com.aajtech.hr.business.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.aajtech.hr.business.api.TemplateManager;
import com.aajtech.hr.model.Template;
import com.aajtech.hr.model.User;

public class TemplateManagerImpl extends BaseJpaManager implements
		TemplateManager {

	@Inject
	public TemplateManagerImpl(Provider<EntityManager> entityManagerProvider) {
		super(entityManagerProvider);
	}

	@Override
	public byte[] buildResume(final User user) {
		return doInJpa(new JpaCallback<byte[]>() {
			@Override
			public byte[] call(EntityManager entityManager) throws IOException {
				// TODO: marcar un template como template por defecot o algo asi
				Template template = entityManager.createQuery(
						"select t from Template t", Template.class)
						.getSingleResult();

				XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(
						template.getFile()));

				for (XWPFParagraph p : doc.getParagraphs()) {
					List<XWPFRun> runs = p.getRuns();
					if (runs != null) {
						for (XWPFRun r : runs) {
							replaceUserData(r, user);
						}
					}
				}
				for (XWPFTable tbl : doc.getTables()) {
					for (XWPFTableRow row : tbl.getRows()) {
						for (XWPFTableCell cell : row.getTableCells()) {
							for (XWPFParagraph p : cell.getParagraphs()) {
								for (XWPFRun r : p.getRuns()) {
									replaceUserData(r, user);
								}
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

	private void replaceUserData(XWPFRun r, User user) {
		replace(r, "headline", user.getHeadline());
		replace(r, "summary", user.getSummary());
	}

	private void replace(XWPFRun r, String template, String value) {
		String text = r.getText(0);
		if (text.contains(template)) {
			text = text.replaceAll(template, value);
			r.setText(text, 0);
		}
	}
}
