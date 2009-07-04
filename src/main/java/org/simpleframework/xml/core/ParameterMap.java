package org.simpleframework.xml.core;

import java.util.Iterator;
import java.util.LinkedHashMap;

class ParameterMap extends LinkedHashMap<String, Parameter> implements Iterable<Parameter>{
    
    public ParameterMap() {
       super();
    }
    
    public Iterator<Parameter> iterator() {
       return values().iterator();
    }

}
