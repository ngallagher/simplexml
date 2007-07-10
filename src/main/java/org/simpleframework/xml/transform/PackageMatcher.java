package org.simpleframework.xml.transform;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

class PackageMatcher implements Matcher {
   
   private Matcher matcher;
   
   public PackageMatcher(Matcher matcher) {
      this.matcher = matcher;
   }
   
   public Transform match(Class type) throws Exception {  
      String name = type.getName();
      
      if(name.startsWith("java.lang")) {
         return matchLanguage(type);
      }
      if(name.startsWith("java.util")) {
         return matchUtility(type);         
      }
      if(name.startsWith("java.net")) {
         return matchURL(type);         
      }
      if(name.startsWith("java.io")) {
         return matchFile(type);         
      }
      if(name.startsWith("java.sql")) {
         return matchDate(type);   
      }     
      return matcher.match(type);
   }
   
   private Transform matchLanguage(Class type) throws Exception {      
      if(type == Boolean.class) {
         return new BooleanTransform();
      }
      if(type == Integer.class) {
         return new IntegerTransform();
      }
      if(type == Long.class) {
         return new LongTransform();
      }
      if(type == Double.class) {
         return new DoubleTransform();
      }
      if(type == Float.class) {
         return new FloatTransform();
      }
      if(type == Short.class) {
         return new ShortTransform();
      }
      if(type == Byte.class) {
         return new ByteTransform();
      }
      if(type == Character.class) {
         return new CharacterTransform();
      }
      return null;
   }
   
   private Transform matchMath(Class type) {
      if(type == BigDecimal.class) {
         return new BigDecimalTransform();
      }
      if(type == BigInteger.class) {
         return new BigIntegerTransform();
      }
      return null;
   }
   
   private Transform matchUtility(Class type) throws Exception {
      if(type == Date.class) {
         return new DateTransform(type);
      }
      if(type == Locale.class) {
         return new LocaleTransform();
      }
      if(type == Currency.class) {
         return new CurrencyTransform();
      }
      if(type == GregorianCalendar.class) {
         return new GregorianCalendarTransform();
      }
      if(type == TimeZone.class) {
         return new TimeZoneTransform();
      } 
      return null;
   }      
   
   private Transform matchDate(Class type) throws Exception {
      if(type == Time.class) {
         return new DateTransform(type);
      }
      if(type == java.sql.Date.class) {
         return new DateTransform(type);
      }
      if(type == Timestamp.class) {
         return new DateTransform(type);
      }
      return null;
   }   
   
   private Transform matchFile(Class type) {
      if(type == File.class) {
         return new FileTransform();
      } 
      return null;      
   }   
   private Transform matchURL(Class type) {
      if(type == URL.class) {
         return new URLTransform();
      }
      return null;      
   }
}
