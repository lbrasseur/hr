package com.aajtech.hr.ioc;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;

import javax.inject.Inject;

import com.google.inject.Injector;
import com.google.inject.TypeLiteral;

public class SerializableProvider<T> implements Serializable {
	private final Class<T> clazz;
//	private transient T instance;

	@SuppressWarnings("unchecked")
	@Inject
	public SerializableProvider(TypeLiteral<T> typeLiteral) {
		this.clazz = (Class<T>) requireNonNull(typeLiteral).getRawType();
	}

	public T get() {
//		if (instance == null) {
//			synchronized (this) {
//				if (instance == null) {
//					instance = injector.getInstance(clazz);
//				}
//			}
//		}
//		return instance;
		return ContextListener.injector.getInstance(clazz);
	}
}
