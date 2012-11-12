package org.simpleframework.xml.core;

import org.simpleframework.xml.util.Cache;
import org.simpleframework.xml.util.ConcurrentCache;

class DetailExtractor {
   
   private final Cache<ContactList> methods;
   private final Cache<ContactList> fields;
   private final Cache<Detail> details;
   private final Support support;
   
   public DetailExtractor(Support support) {
      this.methods = new ConcurrentCache<ContactList>();
      this.fields = new ConcurrentCache<ContactList>();
      this.details = new ConcurrentCache<Detail>();
      this.support = support;
   }
   
   public Detail getDetail(Class type) throws Exception {
      Detail detail = details.fetch(type);
      
      if(detail == null) {
         detail = new DetailScanner(type);
         details.cache(type,  detail);
      }
      return detail;
   }
   
   public ContactList getFields(Class type) throws Exception {
      ContactList list = fields.fetch(type);
      
      if(list == null) {
         Detail detail = getDetail(type);
         
         if(detail != null) {
            list = getFields(detail);
         }
      }
      return list;
   }
   
   private ContactList getFields(Detail detail) throws Exception {
      ContactList list = new FieldScanner(detail, support);
      
      if(detail != null) {
         fields.cache(detail, list);
      }
      return list;
   }
   
   public ContactList getMethods(Class type) throws Exception {
      ContactList list = methods.fetch(type);
      
      if(list == null) {
         Detail detail = getDetail(type);
         
         if(detail != null) {
            list = getMethods(detail);
         }
      }
      return list;
   }
   
   private ContactList getMethods(Detail detail) throws Exception {
      ContactList list = new MethodScanner(detail, support);
      
      if(detail != null) {
         methods.cache(detail, list);
      }
      return list;
   }
}
