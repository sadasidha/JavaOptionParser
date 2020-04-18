package com.simple.mind.optionreader.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Options {
	String[] name() default {};

	boolean optional() default false;

	int maxLength() default 0;

	boolean ignore() default false;

	String[] defaultValues() default {};
}
