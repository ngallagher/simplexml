package com.yieldbroker.common.swing.ui.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This can be used to annotate a parameter in order to identify the
 * component that is to be injected at the annotated parameter index.
 * Component values are typically the Swing components they represent.
 * 
 * @author Niall Gallagher
 * 
 * @see com.yieldbroker.common.swing.ui.Controller
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ComponentOf {
	String value();
}
