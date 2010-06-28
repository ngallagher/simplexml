package org.simpleframework.xml.benchmark.asm.accessor;

public interface PropertyAccessor<S, V> { 
   public void set(S source, V value); 
   public V get(S source); 
} 