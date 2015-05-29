package com.aajtech.hr.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import com.google.appengine.api.datastore.Blob;

@Entity
public class Template implements Serializable {
	@Id
	private String name = "";

	@Lob
	private byte[] file;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}
}
