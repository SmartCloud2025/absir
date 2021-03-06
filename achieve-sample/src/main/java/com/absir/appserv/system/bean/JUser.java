/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-3-10 下午11:30:34
 */
package com.absir.appserv.system.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.absir.appserv.feature.menu.value.MaEntity;
import com.absir.appserv.feature.menu.value.MaMenu;
import com.absir.appserv.system.bean.base.JbUser;
import com.absir.appserv.system.bean.proxy.JpMeta;
import com.absir.appserv.system.bean.proxy.JpUserBase;
import com.absir.appserv.system.bean.value.JaCrud;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.appserv.system.bean.value.JaEdit;
import com.absir.appserv.system.bean.value.JaLang;
import com.absir.appserv.system.bean.value.JeEditable;
import com.absir.appserv.system.crud.PasswordCrudFactory;

/**
 * @author absir
 * 
 */
@SuppressWarnings("serial")
@MaEntity(parent = { @MaMenu("用户管理") }, name = "用户")
@Entity
public class JUser extends JbUser implements JpUserBase, JpMeta, Serializable {

	@JaLang("用户密码")
	@JaEdit(editable = JeEditable.OPTIONAL, types = "password")
	@JaCrud(cruds = { Crud.CREATE, Crud.UPDATE }, factory = PasswordCrudFactory.class)
	@Column(columnDefinition = "char(32)")
	private String password;

	@JaLang("密码加密")
	@JaEdit(editable = JeEditable.DISABLE)
	private String salt;

	@JaLang("激活用户")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private boolean enable;

	@JaLang("过期用户")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private boolean expired;

	@JaLang("锁定用户")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private boolean locked;

	@JaLang("最后登录")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long lastLogin;

	@JaLang("错误登录")
	@JaEdit(groups = JaEdit.GROUP_LIST)
	private int errorLogin;

	@JaLang("最后错误登录")
	@JaEdit(types = "dateTime", groups = JaEdit.GROUP_LIST)
	private long lastErrorLogin;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.absir.appserv.system.bean.proxy.Proxies.JpUserBase#getUserId()
	 */
	@Override
	public Long getUserId() {
		// TODO Auto-generated method stub
		return getId();
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the enable
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * @param enable
	 *            the enable to set
	 */
	public void setEnable(boolean enable) {
		this.enable = enable;
	}

	/**
	 * @return the expired
	 */
	public boolean isExpired() {
		return expired;
	}

	/**
	 * @param expired
	 *            the expired to set
	 */
	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	/**
	 * @return the locked
	 */
	public boolean isLocked() {
		return locked;
	}

	/**
	 * @param locked
	 *            the locked to set
	 */
	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	/**
	 * @return the lastLogin
	 */
	public long getLastLogin() {
		return lastLogin;
	}

	/**
	 * @param lastLogin
	 *            the lastLogin to set
	 */
	public void setLastLogin(long lastLogin) {
		this.lastLogin = lastLogin;
	}

	/**
	 * @return the errorLogin
	 */
	public int getErrorLogin() {
		return errorLogin;
	}

	/**
	 * @param errorLogin
	 *            the errorLogin to set
	 */
	public void setErrorLogin(int errorLogin) {
		this.errorLogin = errorLogin;
	}

	/**
	 * @return the lastErrorLogin
	 */
	public long getLastErrorLogin() {
		return lastErrorLogin;
	}

	/**
	 * @param lastErrorLogin
	 *            the lastErrorLogin to set
	 */
	public void setLastErrorLogin(long lastErrorLogin) {
		this.lastErrorLogin = lastErrorLogin;
	}
}
