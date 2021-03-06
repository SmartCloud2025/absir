/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-6-7 下午5:44:54
 */
package com.absir.appserv.crud;

import com.absir.appserv.system.bean.proxy.JiUserBase;

/**
 * @author absir
 * 
 */
public interface ICrudProcessor {

	/**
	 * @param crudProperty
	 * @param entity
	 * @param handler
	 * @param user
	 */
	public void crud(CrudProperty crudProperty, Object entity, CrudHandler handler, JiUserBase user);
}
