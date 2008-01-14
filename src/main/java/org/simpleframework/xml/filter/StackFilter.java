/*
 * StackFilter.java May 2006
 *
 * Copyright (C) 2006, Niall Gallagher <niallg@users.sf.net>
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

package org.simpleframework.xml.filter;

import java.util.Stack;

/**
 * The <code>StackFilter</code> object provides a filter that can
 * be given a collection of filters which can be used to resolve a
 * replacement. The order of the resolution used for this filter
 * is last in first used. This order allows the highest priority
 * filter to be added last within the stack. 
 * 
 * @author Niall Gallagher 
 */
public class StackFilter implements Filter {

   /**
    * This is used to store the filters that are used.
    */
   private Stack<Filter> stack;        
        
   /**
    * Constructor for the <code>StackFilter</code> object. This will
    * create an empty filter that initially resolves null for all
    * replacements requested. As filters are pushed into the stack
    * the <code>replace</code> method can resolve replacements. 
    */
   public StackFilter() {
      this.stack = new Stack<Filter>();
   }

   /**
    * This pushes the the provided <code>Filter</code> on to the top
    * of the stack. The last filter pushed on to the stack has the
    * highes priority in the resolution of a replacement value.
    * 
    * @param filter this is a filter to be pushed on to the stack
    */
   public void push(Filter filter) {
      stack.push(filter);           
   }
   
   /**
    * Replaces the text provided with the value resolved from the
    * stacked filters. This attempts to resolve a replacement from
    * the top down. So the last <code>Filter</code> pushed on to
    * the stack will be the first filter queried for a replacement.
    * 
    * @param text this is the text value to be replaced
    * 
    * @return this will return the replacement text resolved
    */
   public String replace(String text) {
      for(int i = stack.size(); --i >= 0;) {
         String value = stack.get(i).replace(text);

         if(value != null){
            return value;                 
         }         
      }           
      return null;
   }
}
