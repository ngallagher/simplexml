package org.simpleframework.xml.core;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.simpleframework.xml.strategy.Type;

class DebugLabel implements Label{
   
   private final Map<String, AtomicInteger> map;
   private final Label label;
   public DebugLabel(Label label) {
      this.map = new HashMap<String, AtomicInteger>();
      this.label = label;
   }

   private void showMethodInvocation() {
      Exception e = new Exception();
      StackTraceElement[] list = e.getStackTrace();
      String text = list[1].toString();
      AtomicInteger count = map.get(text);
      if(count == null) {
         count = new AtomicInteger(0);
         map.put(text, count);
      }
      count.getAndIncrement();
      System.err.println(count.get() + " " + text);
   }
   
   public Annotation getAnnotation() {
      showMethodInvocation();
      return label.getAnnotation();
   }

   public Contact getContact() {
      showMethodInvocation();
      return label.getContact();
   }

   public Converter getConverter(Context context) throws Exception {
      showMethodInvocation();
      return label.getConverter(context);
   }

   public Decorator getDecorator() throws Exception {
      showMethodInvocation();
      return label.getDecorator();
   }

   public Type getDependent() throws Exception {
      showMethodInvocation();
      return label.getDependent();
   }

   public Object getEmpty(Context context) throws Exception {
      showMethodInvocation();
      return label.getEmpty(context);
   }

   public String getEntry() throws Exception {
      showMethodInvocation();
      return label.getEntry();
   }

   public Expression getExpression() throws Exception {
      showMethodInvocation();
      return label.getExpression();
   }

   public Label getLabel(Class type) throws Exception {
      showMethodInvocation();
      return label.getLabel(type);
   }

   public String getName(Context context) throws Exception {
      showMethodInvocation();
      return label.getName(context);
   }

   public String getName() throws Exception {
      showMethodInvocation();
      return label.getName();
   }

   public Collection<String> getNames() throws Exception {
      showMethodInvocation();
      return label.getNames();
   }

   public Collection<String> getNames(Context context) throws Exception {
      showMethodInvocation();
      return label.getNames(context);
   }

   public String getOverride() {
      showMethodInvocation();
      return label.getOverride();
   }

   public String getPath(Context context) throws Exception {
      showMethodInvocation();
      return label.getPath();
   }

   public String getPath() throws Exception {
      showMethodInvocation();
      return label.getPath();
   }

   public Collection<String> getPaths() throws Exception {
      showMethodInvocation();
      return label.getPaths();
   }

   public Collection<String> getPaths(Context context) throws Exception {
      showMethodInvocation();
      return label.getPaths(context);
   }

   public Type getType(Class type) throws Exception {
      showMethodInvocation();
      return label.getType(type);
   }

   public Class getType() {
      showMethodInvocation();
      return label.getType();
   }

   public boolean isAttribute() {
      showMethodInvocation();
      return label.isAttribute();
   }

   public boolean isCollection() {
      showMethodInvocation();
      return label.isCollection();
   }

   public boolean isData() {
      showMethodInvocation();
      return label.isData();
   }

   public boolean isInline() {
      showMethodInvocation();
      return label.isInline();
   }

   public boolean isRequired() {
      showMethodInvocation();
      return label.isRequired();
   }

   public boolean isText() {
      showMethodInvocation();
      return label.isText();
   }

   public boolean isUnion() {
      showMethodInvocation();
      return label.isUnion();
   }

}
