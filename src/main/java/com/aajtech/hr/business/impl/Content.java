package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.google.common.collect.Lists;

public class Content {
	private final XWPFDocument doc;
	private final List<XWPFParagraph> paragraphs;

	Content(XWPFDocument doc) {
		this.doc = checkNotNull(doc);
		paragraphs = Lists.newArrayList();

	}

	public void add(String text) {
		if (paragraphs.isEmpty()) {
			XWPFParagraph paragrap = doc.createParagraph();
			paragraphs.add(paragrap);
			XWPFRun run = paragrap.createRun();
			run.setText("", 0);
			paragrap.addRun(run);
		}
		XWPFRun run = getRun();
		run.setText(run.getText(0) + text, 0);
	}

	private XWPFRun getRun() {
		List<XWPFRun> runs = paragraphs.get(paragraphs.size() - 1).getRuns();
		return runs.get(runs.size() - 1);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (XWPFParagraph paragraph : paragraphs) {
			for (XWPFRun run : paragraph.getRuns()) {
				sb.append(run.getText(0));
			}
			sb.append('\n');
		}
		return sb.toString();
	}
}
