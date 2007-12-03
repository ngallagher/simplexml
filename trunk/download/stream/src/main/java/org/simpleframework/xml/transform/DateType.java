/*
 * DateType.java May 2007
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

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;

enum DateType {
   

   FULL("yyyy-MM-dd HH:mm:ss.S z"),
   
   LONG("yyyy-MM-dd HH:mm:ss z"),
   
   NORMAL("yyyy-MM-dd z"),
   
   SHORT("yyyy-MM-dd");

   private DateFormat format;

   private DateType(String format) {
      this.format = new SimpleDateFormat(format);         
   }

   public DateFormat getFormat() {
      return format;         
   }
   
   public static String getText(Date date) throws Exception {
      DateFormat format = FULL.getFormat();
      
      return format.format(date);
   }
   
   public static Date getDate(String text) throws Exception {
      DateType type = getType(text);
      DateFormat format = type.getFormat();
      
      return format.parse(text);
   }

   public static DateType getType(String text) {
      int length = text.length();

      if(length > 23) {
         return FULL;
      }
      if(length > 20) {
         return LONG;
      }
      if(length > 11) {
         return NORMAL;
      }
      return SHORT;
   }
} 