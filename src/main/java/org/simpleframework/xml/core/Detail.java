package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.NamespaceList;
import org.simpleframework.xml.Order;
import org.simpleframework.xml.Root;

interface Detail {
   boolean isEmpty();
   boolean isStrict();
   boolean isRequired();
   boolean isInstantiable();
   boolean isPrimitive();
   Class getSuper();
   Class getType();
   String getName();
   Root getRoot();
   Order getOrder() ;
   DefaultType getAccess();
   Namespace getNamespace();
   NamespaceList getNamespaceList();
   Method[] getMethods();
   Annotation[] getAnnotations();
   Constructor[] getConstructors();
}
