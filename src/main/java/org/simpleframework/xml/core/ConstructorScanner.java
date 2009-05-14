package org.simpleframework.xml.core;

/**
 * Here we scan all constructors that have annotations for the
 * parameters. Each constructor scanned is converted in to a 
 * Builder object as follows.
 * 
 * 
 * public interface Builder {
 * 
 *    public Object build(List<Label> list, List<Object> value);
 * }
 * 
 * The builder looks through the list of labels finds the best 
 * constructor match based on the label names. And then validates
 * the constructor parameter types (WHICH SHOULD REALLY BE VALID
 * ANYWAY BECAUSE THE SCAN PHASE IS RESPONSIBLE FOR THIS). Then 
 * it injects in the values.
 * <p> 
 * Example constructors could be something like.
 * <pre>
 * 
 * @Root
 * public class Property {
 *    private final String name;
 *    private final String value;
 *    
 *    public Property(@Attribute("name") String name,
 *                   @Attribute("value") String value)
 *    {
 *       this.name = name;
 *       this.value = value;
 *    }
 *    
 *    @Resolve
 *    public Property getProperty() {
 *       return null;
 *    }
 * }
 * 
 * @author gallagna
 *
 */
public class ConstructorScanner {

   /**
    * Here we validate to ensure the constructors have correctly
    * annotated the parameters with a matching method or field.
    * If there is no match here then there is an exception. 
    * <p>
    * In particular here we must ensure that IF THE METHOD IS A
    * READ ONLY METHOD THAT THERE IS AT LEAST ONE CONSTRUCTOR THAT
    * WILL ACCEPT THE VALUE.  
    * 
    * @param attributes
    * @param elements
    */
   public void validate(LabelMap attributes, LabelMap elements){
      
   }
}