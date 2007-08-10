<object name="blah">

    <another id="blah" class="X">
        <name text="sma"/>
        <value id="dd" text="de"/>
    </another>

    <another id="abbo" inherit="blah" class="Y">
        <value id="ss" inherit="dd"/>
    </another>

</object>


@Root(inherit=true)
public class Extendable {

    @Element(name="blah")
    public String value;

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





public void commit

public CycleStrategy(String... list) {
    
}



new InheritStrategy("id", "idref", "inherit", "class", "length");


public @interface Inherit {

    public boolean recursive() default false;
}
