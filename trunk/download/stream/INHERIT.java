/**
<object name="blah">
    <object id="blah" class="X">
        <object text="sma"/>
        <object id="dd" text="de"/>
    </object>
    <object id="abbo" inherit="blah" class="Y">
        <object id="ss" inherit="dd"/>
    </object>
</object>
*/



public interface Type {

    public boolean isReference();

    // If this is true then the Composite knows that it does not
    // have to validate all the fields, it only needs to validate
    // to ensure that the values are not null if they are required!!!!!
    public boolean isInherited();
}


@Root
public class Extendable {

    // A required field can now have a default, which will not affect the 
    // semantics of deserialization if there is no XML element for it.
    //
    // Primitives will NOT work with this scheme, we have to have a way
    // of knowing if the serialization will work effectively? Perhaps we
    // could use the session object to store the values.
    //
    // Maybe the node map could assist in allowing the requirement of
    // validation to be conveyed to the persistence engine.    
    @Element(name="blah")
    public String value = "blah";

    @Element(name="sma")
    public int id;

    @Attribute
    public String name;


    @Element
    public String getElement()  {
        return x;
    }

    @Element
    public void setElement(String name) {
        this.name = name;
    }
}

package org.simpleframework.xml.load;

public class Persister {

    // Primitives are copied by the Transformer only, primitives
    // Are pass by value composite objects are pass by reference
    // Or if they are clonable, they could be pass by value
    //
    // Maybe all primitives can be pass by cloning the value???
    public void write(Object source, Object result) throws Exception
}


// This gives a copy of the object in another object, which allows
// the object to be inherited if the clone is fed back in to the
// serialzation process. Perhaps some setting is needed to specify
// that not all of the labels need to be matched.
public class Clone {

    private final Source root;

    public Clone(Source root) {
       this.root = root;
    }

    public void write(Object source, Object clone) throws Exception {        
        Schema schema = root.getSchema(source);
    
        while(Label label : map) {
            Contact label = label.getContact();
            Object value = field.get(source);

            if(value instanceof Clonable) {
                value = clone(value);
            }
            label.set(clone, value);        
        }        
    }

    private void writeElements(Object ) {
    }

    private void writeAttributes() {
    }

    private void writeText() {
    }
}

public @interface Root {

    public boolean strict() default false;
}
