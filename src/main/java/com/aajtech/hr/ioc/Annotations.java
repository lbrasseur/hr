package com.aajtech.hr.ioc;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.inject.Qualifier;

import com.google.inject.BindingAnnotation;

/**
 * Annotations for IoC configuration.
 */
public interface Annotations {
	@Qualifier
	@BindingAnnotation
	@Target({ PARAMETER, METHOD })
	@Retention(RUNTIME)
	public @interface UserId {
		String KEY = "aajUserId";
	}

	@Qualifier
	@BindingAnnotation
	@Target({ PARAMETER, METHOD })
	@Retention(RUNTIME)
	public @interface OAuth2RedirectUrl {
	}
}
