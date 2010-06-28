package org.simpleframework.xml.benchmark.asm.accessor;

public class PurchaseOrder { 
   
   @Property 
   public Customer customer; 
 
   @Property 
   public String name;
   
   public Customer getCustomer() {
      return customer;
   }
} 
