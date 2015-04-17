package com.aajtech.hr.ioc;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import javax.inject.Inject;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

public class SerializableProvider<T> implements Serializable {
	@Inject
	private transient static Injector injector;

	private final Class<T> clazz;

	@SuppressWarnings("unchecked")
	@Inject
	public SerializableProvider(TypeLiteral<T> typeLiteral) {
		this.clazz = (Class<T>) requireNonNull(typeLiteral).getRawType();
	}

	public T get() {
		return injector.getInstance(clazz);
	}
}
