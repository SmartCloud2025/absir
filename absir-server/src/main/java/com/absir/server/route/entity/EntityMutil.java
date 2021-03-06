/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014-1-15 下午12:51:25
 */
package com.absir.server.route.entity;

import com.absir.bean.basis.BeanDefine;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.server.in.Input;
import com.absir.server.route.RouteEntity;

/**
 * @author absir
 * 
 */
public class EntityMutil extends RouteEntity {

	/** beanDefine */
	private BeanDefine beanDefine;

	/**
	 * @param beanDefine
	 */
	public EntityMutil(BeanDefine beanDefine) {
		this.beanDefine = beanDefine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.server.route.RouteEntity#getRouteType()
	 */
	@Override
	public Class<?> getRouteType() {
		// TODO Auto-generated method stub
		return beanDefine.getBeanType();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.server.route.RouteEntity#getRouteBean(com.absir.server.in.Input
	 * )
	 */
	@Override
	public Object getRouteBean(Input input) {
		// TODO Auto-generated method stub
		return beanDefine.getBeanObject(BeanFactoryUtils.get());
	}
}
