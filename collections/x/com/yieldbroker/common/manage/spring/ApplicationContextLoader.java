package com.yieldbroker.common.manage.spring;

import static org.springframework.beans.factory.config.PropertyPlaceholderConfigurer.SYSTEM_PROPERTIES_MODE_FALLBACK;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;

public class ApplicationContextLoader {

	private final DefaultListableBeanFactory beanFactory;
	private final GenericApplicationContext appContext;
	private final Resource[] propertyFiles;

	public ApplicationContextLoader(Resource configFile, Resource... propertyFiles) {
		this.beanFactory = new XmlBeanFactory(configFile);
		this.appContext = new GenericApplicationContext(beanFactory);
		this.propertyFiles = propertyFiles;
	}

	public void start() throws Exception {
		PropertyPlaceholderConfigurer propertyConfigurer = new PropertyPlaceholderConfigurer();
		propertyConfigurer.setLocations(propertyFiles);
		propertyConfigurer.setSystemPropertiesMode(SYSTEM_PROPERTIES_MODE_FALLBACK);
		propertyConfigurer.postProcessBeanFactory(beanFactory);
		appContext.refresh();
	}
	
	public <T> T get(Class<T> type) {
		return appContext.getBean(type);
	}
}
