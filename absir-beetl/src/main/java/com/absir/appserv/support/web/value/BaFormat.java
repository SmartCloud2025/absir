/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月24日 下午1:04:57
 */
package com.absir.appserv.support.web.value;

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
public @interface BaFormat {

	/**
	 * @return
	 */
	String name() default "";

	/**
	 * @return
	 */
	boolean defaultion() default false;

}
