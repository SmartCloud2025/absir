/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-10-9 上午11:11:10
 */
package com.absir.context.core;

import java.io.Serializable;

import com.absir.context.bean.IContext;

/**
 * @author absir
 * 
 */
public abstract class ContextBean<ID extends Serializable> extends Context<ID> implements IContext {

	/** expirationTime */
	protected long expirationTime;

	/**
	 * @return
	 */

	public final boolean isExpiration() {
		return expirationTime == -1;
	}

	/**
	 * 
	 */
	public final void setExpiration() {
		expirationTime = -1;
	}

	/**
	 * @return
	 */
	protected long getLifeTime() {
		return 600000;
	}

	/**
	 * 
	 */
	public void retainAt() {
		retainAt(ContextUtils.getContextTime());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.IContext#retainAt(long)
	 */
	@Override
	public void retainAt(long contextTime) {
		// TODO Auto-generated method stub
		expirationTime = contextTime + getLifeTime();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.IContext#stepDone(long)
	 */
	@Override
	public boolean stepDone(long contextTime) {
		// TODO Auto-generated method stub
		return expirationTime < contextTime;
	}

	/**
	 * @return
	 */
	public Class<?> getKeyClass() {
		return getClass();
	}

	/**
	 * @return
	 */
	public boolean uninitializeDone() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.context.IContext#uninitialize()
	 */
	@Override
	public void uninitialize() {
		// TODO Auto-generated method stub
	}
}
