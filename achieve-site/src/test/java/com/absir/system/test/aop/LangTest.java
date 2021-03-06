/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年9月11日 上午10:12:34
 */
package com.absir.system.test.aop;

import javax.persistence.Embeddable;

import org.junit.Test;

import com.absir.appserv.crud.CrudHandler;
import com.absir.appserv.crud.value.ICrudBean;
import com.absir.appserv.lang.ILangBase;
import com.absir.appserv.lang.LangBundleImpl;
import com.absir.appserv.lang.value.Langs;
import com.absir.appserv.system.bean.value.JaCrud.Crud;
import com.absir.core.base.IBase;
import com.absir.system.test.AbstractTestInject;

/**
 * @author absir
 *
 */
public class LangTest extends AbstractTestInject {

	@Embeddable
	public static class LangEmbed {

		public String name;

		/**
		 * @return the name
		 */
		@Langs
		public String getName() {
			return name;
		}
	}

	public static class LangBean extends LangEmbed implements IBase<Long> {

		public Long id;

		public LangEmbed langEmbed = new LangEmbed();

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.absir.core.base.IBase#getId()
		 */
		@Override
		public Long getId() {
			// TODO Auto-generated method stub
			return id;
		}

		/**
		 * @return the langEmbed
		 */
		public LangEmbed getLangEmbed() {
			return langEmbed;
		}
	}

	@Test
	public void test() throws Throwable {
		try {
			LangBean langBean = new LangBean();
			langBean.id = 3L;
			langBean.name = "测试";
			langBean.getLangEmbed().name = "测试子";
			langBean = LangBundleImpl.ME.getLangProxy("LangBean", langBean);

			CrudHandler crudHandler = new CrudHandler(null, null, null, null, langBean) {
			};
			((ICrudBean) langBean).proccessCrud(Crud.CREATE, crudHandler);

			((ILangBase) langBean).setLang("name", 33, "test");
			((ILangBase) langBean.getLangEmbed()).setLang("name", 33, "test123333");
			((ICrudBean) langBean).proccessCrud(Crud.CREATE, crudHandler);
			((ICrudBean) langBean.getLangEmbed()).proccessCrud(Crud.CREATE, crudHandler);

			ILangBase langBase = (ILangBase) langBean.getLangEmbed();
			System.out.println(langBase.getLang("name", 33, String.class));

			langBean = new LangBean();
			langBean.id = 3L;
			langBean.name = "测试";
			langBean.name = "测试子";
			langBean = LangBundleImpl.ME.getLangProxy("LangBean", langBean);

			System.out.println(langBean.getName());
			System.out.println(langBean.getLangEmbed().getName());
			System.out.println(((ILangBase) langBean).getLang("name", 33, String.class));
			langBase = (ILangBase) langBean.getLangEmbed();
			System.out.println(langBase.getLang("name", 33, String.class));

		} catch (Throwable e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
