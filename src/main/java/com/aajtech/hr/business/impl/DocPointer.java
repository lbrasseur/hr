package com.aajtech.hr.business.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Objects;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import com.google.common.base.Function;
import com.google.common.base.Optional;

class DocPointer {
	private final XWPFDocument doc;
	private int paragraphPos;
	private int runPos;
	private int charPos;

	DocPointer(XWPFDocument doc, int paragraphPos, int runPos, int charPos) {
		this.doc = checkNotNull(doc);
		this.paragraphPos = paragraphPos;
		this.runPos = runPos;
		this.charPos = charPos;
	}

	String getString(DocPointer end) {
		checkNotNull(end);
		checkArgument(doc.equals(end.doc));
		if (paragraphPos == end.paragraphPos && runPos == end.runPos) {
			return getText().substring(charPos, end.charPos);
		} else {
			StringBuilder sb = new StringBuilder();

			sb.append(getText().substring(charPos));
			int currentParagrah = paragraphPos;
			for (DocPointer p = clone().increaseRun(); !p.sameRun(end); p
					.increaseRun()) {
				if (currentParagrah != p.paragraphPos) {
					sb.append('\n');
					currentParagrah = p.paragraphPos;
				}
				sb.append(p.getText());
			}
			sb.append(end.getText().substring(0, end.charPos));
			return sb.toString();
		}
	}

	public void replace(DocPointer end, String value) {
		checkArgument(doc.equals(end.doc));
		if (paragraphPos == end.paragraphPos && runPos == end.runPos) {
			setText(getText().substring(0, charPos) + value
					+ getText().substring(end.charPos));
		} else {
			setText(getText().substring(0, charPos) + value);
			if (end.isValid()) {
				end.setText(end.getText().substring(end.charPos));
			}

			for (DocPointer p = clone().increaseRun(); !p.sameRun(end); p
					.increaseRun()) {
				p.setText("");
			}
		}
	}

	public DocPointer increase() {
		charPos++;
		while (getParagraph().isPresent() && charPos >= getText().length()) {
			increaseRun();
		}
		return this;
	}

	private DocPointer increaseRun() {
		runPos++;
		charPos = 0;
		while (getParagraph().isPresent() && !getRun().isPresent()) {
			increaseParagraph();
		}
		return this;
	}

	private DocPointer increaseParagraph() {
		paragraphPos++;
		runPos = 0;
		charPos = 0;
		return this;
	}

	public boolean isValid() {
		return getParagraph().isPresent() && getRun().isPresent()
				&& charPos < getText().length();
	}

	Optional<XWPFParagraph> getParagraph() {
		return Optional
				.fromNullable(paragraphPos < doc.getParagraphs().size() ? doc
						.getParagraphs().get(paragraphPos) : null);
	}

	Optional<XWPFRun> getRun() {
		return getParagraph().transform(
				new Function<XWPFParagraph, Optional<XWPFRun>>() {
					@Override
					public Optional<XWPFRun> apply(XWPFParagraph p) {
						return Optional.fromNullable(runPos < p.getRuns()
								.size() ? p.getRuns().get(runPos) : null);
					}
				}).or(Optional.<XWPFRun> fromNullable(null));
	}

	char getChar() {
		return getText().charAt(charPos);
	}

	private String getText() {
		return getRun().transform(new Function<XWPFRun, String>() {
			@Override
			public String apply(XWPFRun r) {
				return r.getText(0);
			}
		}).or("");
	}

	private void setText(String text) {
		getRun().get().setText(text, 0);
	}

	@Override
	public DocPointer clone() {
		return new DocPointer(doc, paragraphPos, runPos, charPos);
	}

	@Override
	public int hashCode() {
		return Objects.hash(doc, paragraphPos, runPos, charPos);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		DocPointer that = (DocPointer) o;
		return sameRun(that) && Objects.equals(charPos, that.charPos);
	}

	private boolean sameRun(DocPointer other) {
		if (this == other) {
			return true;
		}
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		return Objects.equals(doc, other.doc)
				&& Objects.equals(paragraphPos, other.paragraphPos)
				&& Objects.equals(runPos, other.runPos);
	}
}
