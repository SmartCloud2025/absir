/**
 * Copyright 2013 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2013-5-31 下午4:59:14
 */
package com.absir.appserv.system.service;

import java.io.Serializable;
import java.util.Map;

import org.hibernate.Session;

import com.absir.appserv.system.bean.JPlatformUser;
import com.absir.appserv.system.bean.JSession;
import com.absir.appserv.system.bean.JUser;
import com.absir.appserv.system.bean.proxy.JiUserBase;
import com.absir.appserv.system.crud.PasswordCrudFactory;
import com.absir.appserv.system.dao.BeanDao;
import com.absir.appserv.system.dao.JUserDao;
import com.absir.appserv.system.dao.utils.QueryDaoUtils;
import com.absir.appserv.system.security.ISecuritySupply;
import com.absir.appserv.system.security.SecurityContext;
import com.absir.appserv.system.security.SecurityManager;
import com.absir.bean.inject.value.Bean;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.value.Schedule;
import com.absir.core.kernel.KernelObject;
import com.absir.orm.transaction.value.Transaction;

/**
 * @author absir
 * 
 */
@SuppressWarnings("unchecked")
@Bean
public class SecurityServiceImpl extends SecurityService implements ISecuritySupply {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecurityService#getUserBase(java.lang
	 * .Long)
	 */
	@Transaction(readOnly = true)
	@Override
	public JiUserBase getUserBase(Long userId) {
		// TODO Auto-generated method stub
		return loadUser(BeanDao.get(BeanDao.getSession(), JUser.class, userId));
	}

	/**
	 * @param user
	 * @return
	 */
	private JUser loadUser(JUser user) {
		if (user != null) {
			user.getUserRoles().isEmpty();
		}

		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecurityService#getUserBase(java.lang
	 * .String)
	 */
	@Transaction(readOnly = true)
	@Override
	public JiUserBase getUserBase(String username) {
		// TODO Auto-generated method stub
		return loadUser(JUserDao.ME.findByUsername(username));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecurityService#validator(com.absir
	 * .appserv.system.bean.proxy.JiUserBase, java.lang.String)
	 */
	@Override
	public boolean validator(JiUserBase userBase, String password) {
		// TODO Auto-generated method stub
		if (password == null || !(userBase instanceof JUser)) {
			return true;
		}

		JUser user = (JUser) userBase;
		long contextTime = ContextUtils.getContextTime();
		if (user.getLastErrorLogin() > contextTime) {
			return false;
		}

		if (PasswordCrudFactory.getPasswordEncrypt(password, user.getSalt()).equals(user.getPassword())) {
			return true;
		}

		int errorLogin = user.getErrorLogin() + 1;
		if (errorLogin > 5) {
			// 密码错误5次,30分钟内禁止登录
			errorLogin = 0;
			user.setLastErrorLogin(contextTime + 1800000);
		}

		user.setErrorLogin(errorLogin);
		BeanService.ME.merge(user);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecurityService#openUserBase(java.
	 * lang.String, java.lang.String, java.lang.String)
	 */
	@Transaction(rollback = Throwable.class)
	@Override
	public JiUserBase openUserBase(String username, String password, String platform) {
		// TODO Auto-generated method stub
		JPlatformUser user = JUserDao.ME.findByPlatformUsername(platform, username);
		if (user == null) {
			user = new JPlatformUser();
			user.setPlatform(platform);
			user.setUsername(username);
			Session session = BeanDao.getSession();
			try {
				session.persist(user);

			} catch (Throwable e) {
				// TODO: handle exception
				session.clear();
				user = JUserDao.ME.findByPlatformUsername(platform, username);
			}
		}

		return user;
	}

	/**
	 * @param userBase
	 * @return
	 */
	protected SecurityContext createSecurityContext(JiUserBase userBase, String sessionId) {
		SecurityContext securityContext = ContextUtils.getContext(SecurityContext.class, sessionId);
		if (userBase.getClass() == JUser.class) {
			securityContext.setSecuritySupply(this);
		}

		return securityContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.SecurityService#setSecurityContext(com
	 * .absir.appserv.system.security.SecurityContext,
	 * com.absir.appserv.system.bean.proxy.JiUserBase)
	 */
	@Override
	protected void setSecurityContext(SecurityContext securityContext, JiUserBase userBase) {
		// TODO Auto-generated method stub
		if (userBase.getClass() == JUser.class) {
			securityContext.setSecuritySupply(this);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.service.SecurityService#findSecurityContext(
	 * java.lang.String, com.absir.appserv.system.security.SecurityManager)
	 */

	@Override
	@Transaction(readOnly = true)
	protected SecurityContext findSecurityContext(String sessionId, SecurityManager securityManager) {
		// TODO Auto-generated method stub
		Session session = BeanDao.getSession();
		JSession jSession = BeanDao.get(session, JSession.class, sessionId);
		if (jSession != null && jSession.getPassTime() >= ContextUtils.getContextTime()) {
			JUser user = BeanDao.get(session, JUser.class, jSession.getUserId());
			if (user != null) {
				SecurityContext securityContext = ContextUtils.getContext(SecurityContext.class, sessionId);
				securityContext.setUser(loadUser(user));
				if (jSession.getMetas() != null) {
					Object metas = KernelObject.unserialize(jSession.getMetas());
					if (metas != null && metas instanceof Map) {
						securityContext.setMetas((Map<String, Serializable>) metas);
					}
				}

				securityContext.setLifeTime(securityManager.getSessionLife());
				securityContext.setMaxExpirationTime(jSession.getPassTime());
				return securityContext;
			}
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.absir.appserv.system.security.ISecuritySupply#saveSession(com.absir
	 * .appserv.system.security.SecurityContext)
	 */
	@Override
	public void saveSession(SecurityContext securityContext) {
		// TODO Auto-generated method stub
		JSession session = new JSession();
		session.setId(securityContext.getId());
		JiUserBase userBase = securityContext.getUser();
		session.setUserId(userBase.getUserId());
		session.setUsername(userBase.getUsername());
		session.setAddress(securityContext.getAddress());
		session.setAgent(securityContext.getAgent());
		session.setLastTime(ContextUtils.getContextTime());
		session.setPassTime(securityContext.getMaxExpirationTime());
		if (securityContext.getMetas() != null) {
			session.setMetas(KernelObject.serialize(securityContext.getMetas()));
		}

		BeanService.ME.merge(session);
	}

	/**
	 * 清除过期登录
	 */
	@Schedule(fixedDelay = 24 * 36000)
	@Transaction
	protected void removeExpiredSession() {
		QueryDaoUtils.createQueryArray(BeanDao.getSession(), "DELETE FROM JSession o WHERE o.passTime < ?", ContextUtils.getContextTime()).executeUpdate();
	}

}
