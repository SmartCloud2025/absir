/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-21 上午10:59:40
 */
package com.absir.appserv.game.value;

import com.absir.appserv.system.context.value.ObjectParameters;

/**
 * @author absir
 * 
 */
@SuppressWarnings("rawtypes")
public abstract class OTrigger<T extends OObject> extends ObjectParameters {

	/**
	 * @param parameters
	 */
	public OTrigger(String[] parameters) {
		super(parameters);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param self
	 * @return
	 */
	public abstract boolean isTrigger(T self);

}
