package com.yieldbroker.common.manage.spring;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jmx.export.annotation.ManagedAttribute;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedResource;

import com.yieldbroker.common.time.DateTime;
import com.yieldbroker.common.time.DateTime.Duration;

@ManagedResource(description = "Contains application details")
public class ApplicationInfo implements ApplicationContextAware {

	private ApplicationContext context;
	private DateTime startTime;

	public ApplicationInfo() {
		this.startTime = DateTime.now();
	}

	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}

	@ManagedAttribute(description = "Current working directory")
	public String getWorkingDirectory() {
		return new File(".").getAbsolutePath();
	}

	@ManagedAttribute(description = "Time elapsed since start up")
	public String getUpTime() {
		DateTime dateTime = DateTime.now();
		Duration timeElapsed = dateTime.timeDifference(startTime);
		return timeElapsed.toString();
	}

	@ManagedAttribute(description = "Time on the host machine")
	public String getHostTime() {
		DateTime currentTime = DateTime.now();
		return currentTime.formatDate("dd/MM/yyyy HH:mm:ss");
	}

	@ManagedAttribute(description = "Time on the current host")
	public String getHostTimeZone() {
		return TimeZone.getDefault().getID();
	}
	
	@ManagedOperation(description = "Shows how much memory is used")
	public String showMemoryUsage() {
		List<MemoryPoolMXBean> memoryPools = ManagementFactory.getMemoryPoolMXBeans();
		DecimalFormat format = new DecimalFormat("#,###,###,###");
		StringBuilder builder = new StringBuilder();
		
		builder.append("<TABLE BORDER='1'>");
		builder.append("<TH>name</TH>");
		builder.append("<TH>size</TH>");
		builder.append("<TH>used</TH>");
		builder.append("<TH>free</TH>");
		builder.append("<TH>usage</TH>");		
		
		for(MemoryPoolMXBean memoryPool : memoryPools) {
			String name = memoryPool.getName();
			MemoryUsage usage = memoryPool.getUsage();
			double memoryUsed = usage.getUsed();
			double memoryMax = usage.getMax();
			double memoryFree = memoryMax - memoryUsed;
			double widthMax = 700;
			double widthUsed = widthMax * (memoryUsed / memoryMax);
			double widthFree = widthMax - widthUsed;
			String formattedMax = format.format(memoryMax / 1024);
			String formattedUsed = format.format(memoryUsed / 1024);
			String formattedFree = format.format(memoryFree / 1024);
			String sizeUsed = format.format(widthUsed);
			String sizeFree = format.format(widthFree);
			
			builder.append("<TR>");
			builder.append("<TD>").append(name).append("</TD>");
			builder.append("<TD>").append(formattedMax).append("k</TD>");
			builder.append("<TD>").append(formattedUsed).append("k</TD>");
			builder.append("<TD>").append(formattedFree).append("k</TD>");
			builder.append("<TD>\n");
			builder.append("<TABLE CELLPADDING='0' CELLSPACING='0'>\n");
			builder.append("<TR>\n");
			builder.append("<TD BGCOLOR='#00ff00' HEIGHT='20' WIDTH='").append(sizeUsed).append("'></TD>\n");
			builder.append("<TD BGCOLOR='#ff0000' HEIGHT='20' WIDTH='").append(sizeFree).append("'></TD>\n");
			builder.append("</TABLE>\n");			
			builder.append("</TD>\n");			
			builder.append("</TR>");
		} 
		builder.append("</TABLE>");
		return builder.toString();		
	}

	@ManagedOperation(description = "Show registered beans")
	public String showRegisteredBeans() {
		String[] beanNames = context.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		StringBuilder builder = new StringBuilder();

		builder.append("<TABLE BORDER='1'>");
		builder.append("<TH>name</TH>");
		builder.append("<TH>type</TH>");

		for (String beanName : beanNames) {
			Class<?> beanType = context.getType(beanName);
			String beanClassName = beanType.getName();

			builder.append("<TR>");
			builder.append("<TD>").append(beanName).append("</TD>");
			builder.append("<TD>").append(beanClassName).append("</TD>");
			builder.append("</TR>");
		}
		builder.append("</TABLE>");
		return builder.toString();
	}

	@ManagedOperation(description = "Show system properties")
	public String showSystemProperties() {
		Properties properties = System.getProperties();
		Set<String> propertyKeys = properties.stringPropertyNames();
		Set<String> sortedKeys = new TreeSet<String>(propertyKeys);
		StringBuilder builder = new StringBuilder();

		builder.append("<TABLE BORDER='1'>");
		builder.append("<TH>property</TH>");
		builder.append("<TH>value</TH>");

		for (String propertyKey : sortedKeys) {
			String propertyValue = properties.getProperty(propertyKey);

			builder.append("<TR>");
			builder.append("<TD>").append(propertyKey).append("</TD>");
			builder.append("<TD>").append(propertyValue).append("</TD>");
			builder.append("</TR>");
		}
		builder.append("</TABLE>");
		return builder.toString();
	}
}
