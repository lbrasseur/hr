package com.aajtech.hr.business.impl;

import java.util.List;
import java.util.Objects;

import org.apache.poi.xwpf.usermodel.XWPFRun;

class RunPointer implements Cloneable {
	private final List<XWPFRun> runs;
	private int runPos;
	private int charPos;

	RunPointer(List<XWPFRun> runs) {
		this.runs = runs;
	}

	private RunPointer(List<XWPFRun> runs, int runPos, int charPos) {
		this.runs = runs;
		this.runPos = runPos;
		this.charPos = charPos;
	}

	public RunPointer increase() {
		charPos++;
		if (charPos == getText().length()) {
			increaseRun();
		}
		return this;
	}

	private RunPointer increaseRun() {
		runPos++;
		charPos = 0;
		return this;
	}

	public boolean isValid() {
		return runPos < runs.size() && charPos < getText().length();
	}

	public XWPFRun getRun() {
		return runs.get(runPos);
	}

	public char getChar() {
		return getText().charAt(charPos);
	}

	public String getString(RunPointer endRun) {
		if (runPos == endRun.runPos) {
			return getText().substring(charPos, endRun.charPos);
		} else {
			StringBuilder sb = new StringBuilder();

			sb.append(getText().substring(charPos));
			for (RunPointer p = clone().increaseRun(); p.runPos < endRun.runPos; p
					.increaseRun()) {
				sb.append(p.getText());
			}
			sb.append(endRun.getText().substring(0, endRun.charPos));
			return sb.toString();
		}
	}

	public void replace(RunPointer endRun, String value) {
		if (runPos == endRun.runPos) {
			setText(getText().substring(0, charPos) + value
					+ getText().substring(endRun.charPos));
		} else {
			setText(getText().substring(0, charPos) + value);
			if (endRun.isValid()) {
				endRun.setText(endRun.getText().substring(endRun.charPos));
			}

			for (RunPointer p = clone().increaseRun(); p.runPos < endRun.runPos; p
					.increaseRun()) {
				p.setText("");
			}
		}
	}

	private String getText() {
		return getRun().getText(0);
	}

	private void setText(String text) {
		getRun().setText(text, 0);
	}

	@Override
	public RunPointer clone() {
		return new RunPointer(runs, runPos, charPos);
	}

	@Override
	public int hashCode() {
		return Objects.hash(runs, runPos, charPos);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		RunPointer that = (RunPointer) o;
		return Objects.equals(runs, that.runs)
				&& Objects.equals(runPos, that.runPos)
				&& Objects.equals(charPos, that.charPos);
	}
}