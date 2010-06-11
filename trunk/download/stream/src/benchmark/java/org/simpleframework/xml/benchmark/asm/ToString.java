package org.simpleframework.xml.benchmark.asm;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation for field to be used in <code>toString()</code> construction.
 */
@Target({ElementType.FIELD})
public @interface ToString {
    int order() default 0;
    String text() default "";
}