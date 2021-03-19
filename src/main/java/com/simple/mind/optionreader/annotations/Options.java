package com.simple.mind.optionreader.annotations;

import java.lang.annotation.ElementType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Options {

	/**
	 * Define the option name that will be read, one or more name can be assigned
	 * 
	 * @return
	 */
	String name() default "";

	/**
	 * Optional will be set only if exists; ignored otherwise, <br>
	 * optional and defaultValues should not be set together
	 * 
	 * @return
	 */
	boolean optional() default false;

	/**
	 * Variable will be completely ignored; error will be thrown if set
	 */
	boolean ignore() default false;

	/**
	 * default value will be set if no value is passed from command lines. <br>
	 * multiple value will be acceptable only if the variable is array, list or
	 * listArray
	 */
	String[] defaultValues() default {};
}
