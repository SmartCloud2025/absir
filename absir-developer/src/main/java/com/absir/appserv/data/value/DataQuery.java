/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-3-13 下午5:16:36
 */
package com.absir.appserv.data.value;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author absir
 * 
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataQuery {

	/**
	 * @return
	 */
	String value();

	/**
	 * @return
	 */
	boolean nativeQuery() default false;

	/**
	 * @return
	 */
	boolean cacheable() default false;

	/**
	 * @return
	 */
	Class<?> aliasType() default void.class;
}
