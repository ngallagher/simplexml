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

package simple.xml.transform.util;

import simple.xml.transform.Transform;
import java.util.GregorianCalendar;
import java.util.Date;

public class GregorianCalendarTransform implements Transform<GregorianCalendar> {
   
   private DateTransform transform;
   
   public GregorianCalendarTransform() {
      this.transform = new DateTransform();
   }
   
   public GregorianCalendar read(String value) throws Exception {
      return read(transform.read(value));      
   }
   
   private GregorianCalendar read(Date value) throws Exception {
      GregorianCalendar calendar = new GregorianCalendar();
      
      if(value != null) {
         calendar.setTime(value);
      }
      return calendar;
   }
   
   public String write(GregorianCalendar value) throws Exception {
      return transform.write(value.getTime());
   }
}
