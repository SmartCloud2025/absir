/**
 * Copyright 2014 ABSir's Studio
 * 
 * All right reserved
 *
 * Create on 2014年7月16日 上午10:38:20
 */
package com.absir.context.lang;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import com.absir.bean.basis.Base;
import com.absir.bean.basis.Environment;
import com.absir.bean.core.BeanConfigImpl;
import com.absir.bean.core.BeanFactoryUtils;
import com.absir.bean.inject.value.Bean;
import com.absir.bean.inject.value.Inject;
import com.absir.bean.inject.value.Stopping;
import com.absir.bean.inject.value.Value;
import com.absir.context.core.ContextUtils;
import com.absir.context.schedule.cron.CronFixDelayRunable;
import com.absir.core.kernel.KernelString;

/**
 * @author absir
 *
 */
@SuppressWarnings("unchecked")
@Base
@Bean
public class LangBundle {

	/** ME */
	public static final LangBundle ME = BeanFactoryUtils.get(LangBundle.class);

	/** i18n */
	@Value(value = "lang.i18n", defaultValue = "0")
	private boolean i18n;

	/** langResource */
	@Value(value = "lang.resouce", defaultValue = "${classPath}lang/")
	protected String langResource;

	/** reloadTime */
	protected long reloadTime;

	/** reloadRunable */
	protected CronFixDelayRunable reloadRunable;

	/** local */
	protected Locale locale;

	/** locales */
	protected List<Locale> locales;

	/** resourceBundle */
	protected Map<String, String> resourceBundle;

	/** resourceLangs */
	protected Map<String, String> resourceLangs = new HashMap<String, String>();

	/** localeMapResourceBunlde */
	protected Map<Locale, Map<String, String>> localeMapResourceBunlde = new HashMap<Locale, Map<String, String>>();

	/**
	 * @param reloadTime
	 *            the reloadTime to set
	 */
	public void setReloadTime(long reloadTime) {
		if (this.reloadTime != reloadTime) {
			this.reloadTime = reloadTime;
			if (reloadTime <= 0) {
				if (reloadRunable != null) {
					reloadRunable.setFixDelay(0);
					reloadRunable = null;
				}

			} else {
				reloadRunable = reloadRunable == null ? new CronFixDelayRunable(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						resourceBundle = null;
						localeMapResourceBunlde.clear();
					}

				}, reloadTime) : reloadRunable.transformCronFixDelayRunable(reloadTime);
				ContextUtils.getScheduleFactory().addScheduleRunable(reloadRunable);
			}
		}
	}

	/**
	 * 初始化国际化资源
	 */
	@Inject
	protected void initBundle() {
		Long reload = BeanFactoryUtils.getBeanConfigValue("local.reload", Long.class);
		setReloadTime(reload == null ? BeanFactoryUtils.getEnvironment().compareTo(Environment.DEVELOP) <= 0 ? -1 : 0 : reload);
		String language = BeanFactoryUtils.getBeanConfigValue("local.language", String.class);
		String country = BeanFactoryUtils.getBeanConfigValue("local.country", String.class);
		String variant = BeanFactoryUtils.getBeanConfigValue("local.variant", String.class);
		locale = language == null || country == null ? Locale.getDefault() : new Locale(language, country, variant);
	}

	/**
	 * 内置国际化资源写入
	 */
	@Stopping
	public void stopping() {
		if (!resourceLangs.isEmpty()) {
			String var = locale.getLanguage();
			if (!KernelString.isEmpty(var)) {
				String resource = langResource + var;
				var = locale.getCountry();
				if (!KernelString.isEmpty(var)) {
					resource += '_' + var;
					var = locale.getVariant();
					if (!KernelString.isEmpty(var)) {
						resource += '_' + var;
					}
				}

				BeanConfigImpl.writeProperties(resourceLangs, new File(resource + "/general.properties"));
			}
		}
	}

	/**
	 * @return the i18n
	 */
	public boolean isI18n() {
		return i18n;
	}

	/**
	 * @return
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @return
	 */
	public Locale getLocale(int index) {
		if (locales == null || index < 0 || index >= locales.size()) {
			return locale;
		}

		return locales.get(index);
	}

	/**
	 * @return
	 */
	public Map<String, String> getResourceBundle() {
		if (reloadTime < 0) {
			return getResourceBundle(locale);
		}

		if (resourceBundle == null) {
			resourceBundle = getResourceBundle(locale);
		}

		return resourceBundle;
	}

	/**
	 * @param name
	 * @param lang
	 */
	public void setResourceLang(String name, String lang) {
		Map<String, String> resourceBundle = getResourceBundle();
		if (!resourceBundle.containsKey(name)) {
			resourceLangs.put(name, lang);
			resourceBundle.put(name, lang);
		}
	}

	/**
	 * @param locale
	 * @return
	 */
	public Map<String, String> getResourceBundle(Locale locale) {
		Map<String, String> resourceBundle = localeMapResourceBunlde.get(locale);
		if (resourceBundle != null) {
			return resourceBundle;
		}

		String var = locale.getLanguage();
		if (!KernelString.isEmpty(var)) {
			String resource = langResource + var;
			File resourceFile = new File(resource);
			if (resourceFile.exists()) {
				resourceBundle = new HashMap<String, String>();
				BeanConfigImpl.readDirProperties(null, (Map<String, Object>) (Object) resourceBundle, resourceFile, null);
				if (reloadTime >= 0) {
					localeMapResourceBunlde.put(locale, resourceBundle);
				}

				var = locale.getCountry();
				if (!KernelString.isEmpty(var)) {
					resource += '_' + var;
					resourceFile = new File(resource);
					if (resourceFile.exists()) {
						BeanConfigImpl.readDirProperties(null, (Map<String, Object>) (Object) resourceBundle, resourceFile, null);
						var = locale.getVariant();
						if (!KernelString.isEmpty(var)) {
							resource += '_' + var;
							resourceFile = new File(resource);
							if (resourceFile.exists()) {
								BeanConfigImpl.readDirProperties(null, (Map<String, Object>) (Object) resourceBundle, resourceFile, null);
							}
						}
					}
				}
			}
		}

		if (locale == this.locale) {
			if (resourceBundle == null) {
				resourceBundle = new HashMap<String, String>();
			}

			this.resourceBundle = resourceBundle;
			for (Entry<String, String> entry : resourceLangs.entrySet()) {
				if (!resourceBundle.containsKey(entry.getKey())) {
					resourceBundle.put(entry.getKey(), entry.getValue());
				}
			}

			return resourceBundle;
		}

		if (resourceBundle == null) {
			resourceBundle = getResourceBundle();
		}

		return resourceBundle;
	}

	/**
	 * 
	 */
	public void clearResouceBundle() {
		resourceBundle = null;
		localeMapResourceBunlde.clear();
	}

	/**
	 * @param lang
	 * @param locale
	 * @return
	 */
	public String getLangResource(String lang, Locale locale) {
		return getLangResource(lang, getResourceBundle(locale), locale);
	}

	/**
	 * @param lang
	 * @param locale
	 * @return
	 */
	public String getLangResource(String lang, Map<String, String> resourceBundle, Locale locale) {
		String value = resourceBundle.get(lang);
		if (value == null) {
			value = lang;
		}

		return value;
	}
}
