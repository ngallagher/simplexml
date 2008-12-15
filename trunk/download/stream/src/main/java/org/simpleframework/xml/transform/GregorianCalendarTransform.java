/*
 * GregorialCalendarTransform.java May 2007
 *
 * Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the 
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General 
 * Public License along with this library; if not, write to the 
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330, 
 * Boston, MA  02111-1307  USA
 */

package org.simpleframework.xml.transform;

import java.util.GregorianCalendar;
import java.util.Date;

/**
 * The <code>DateTransform</code> is used to transform calendar
 * values to and from string representations, which will be inserted
 * in the generated XML document as the value place holder. The
 * value must be readable and writable in the same format. Fields
 * and methods annotated with the XML attribute annotation will use
 * this to persist and retrieve the value to and from the XML source.
 * <pre>
 * 
 *    &#64;Attribute
 *    private GregorianCalendar date;
 *    
 * </pre>
 * As well as the XML attribute values using transforms, fields and
 * methods annotated with the XML element annotation will use this.
 * Aside from the obvious difference, the element annotation has an
 * advantage over the attribute annotation in that it can maintain
 * any references using the <code>CycleStrategy</code> object. 
 * 
 * @author Niall Gallagher
 */
class GregorianCalendarTransform implements Transform<GregorianCalendar> {
   
   /**
    * This is the date transform used to parse and format dates.
    */  
   private final DateTransform transform;
   
   /**
    * Constructor for the <code>GregorianCalendarTransform</code> 
    * object. This is used to create a transform using a default 
    * date format pattern. The format chosen for the default date 
    * uses <code>2007-05-02 12:22:10.000 GMT</code> like dates.
    */
   public GregorianCalendarTransform() throws Exception {
      this(Date.class);
   }
   
   /**
    * Constructor for the <code>GregorianCalendarTransform</code> 
    * object. This is used to create a transform using a default 
    * date format pattern. The format should typically contain 
    * enough information to create the date using a different 
    * locale or time zone between read and write operations.
    * 
    * @param type this is the type of date to be transformed
    */
   public GregorianCalendarTransform(Class type) throws Exception {
      this.transform = new DateTransform(type);      
   }
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param date the string representation of the date value 
    * 
    * @return this returns an appropriate instanced to be used
    */  
   public GregorianCalendar read(String date) throws Exception {
      return read(transform.read(date));      
   }
   
   /**
    * This method is used to convert the string value given to an
    * appropriate representation. This is used when an object is
    * being deserialized from the XML document and the value for
    * the string representation is required.
    * 
    * @param date the string representation of the date value 
    * 
    * @return this returns an appropriate instanced to be used
    */  
   private GregorianCalendar read(Date date) throws Exception {
      GregorianCalendar calendar = new GregorianCalendar();
      
      if(date != null) {
         calendar.setTime(date);
      }
      return calendar;
   }
   
   /**
    * This method is used to convert the provided value into an XML
    * usable format. This is used in the serialization process when
    * there is a need to convert a field value in to a string so 
    * that that value can be written as a valid XML entity.
    * 
    * @param date this is the value to be converted to a string
    * 
    * @return this is the string representation of the given date
    */
   public String write(GregorianCalendar date) throws Exception {
      return transform.write(date.getTime());
   }
}