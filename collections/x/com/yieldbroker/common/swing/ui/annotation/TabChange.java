package com.yieldbroker.common.swing.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This can be used to annotate a method that is to be invoked when
 * a tab has been clicked on to change the viewable screen.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.swing.ui.Controller
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TabChange {
	String value();
}
