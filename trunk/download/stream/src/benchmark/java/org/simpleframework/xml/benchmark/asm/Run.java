package org.simpleframework.xml.benchmark.asm;

/*
J2SE 5.0 adds many features to the Java platform. I'm not personally convinced that all 
these additions are really improvements. However, two little-noticed new features that 
are truly useful for classworking are the java.lang.instrument package and JVM interface, 
which let you (among other things) specify class transformation agents to be used when 
executing a program.

To use a transformation agent, you need to specify the agent class when you start the JVM. 
When using the java command to launch the JVM, you can specify agents using command line 
parameters of the form -javaagent:jarpath[=options], where "jarpath" is the path to the JAR 
file containing the agent class, and "options" is a parameter string for the agent. The 
agent JAR file uses a special manifest attribute to specify the actual agent class, which 
must define a method public static void premain(String options, Instrumentation inst). This 
agent premain() method will be called before the application's main() method and is able to 
register an actual transformer with the passed-in java.lang.instrument.Instrumentation class 
instance.

The transformer class must implement the java.lang.instrument.ClassFileTransformer interface, 
which defines a single transform() method. When a transformer instance is registered with 
the Instrumentation class instance, that transformer instance will be called for each class 
being created in the JVM. The transformer gets access to the binary class representation and 
can modify the class representation before it is loaded by the JVM.

Listing 4 gives the agent and transformer class (both the same class in this case, though 
they don't need to be) implementation for processing the annotations. The transform() 
implementation uses ASM to scan the supplied binary class representation and look for the 
appropriate annotations, collecting information about the annotated fields of the class. 
If annotated fields are found, the class is modified to include a generated toString() 
method and the modified binary representation is returned. Otherwise the transform() method 
just returns null to indicate that no modifications are necessary.

java -cp C:\work\development\codesearch\serialization\lib\asm-3.3.jar;C:\work\development\codesearch\serialization\lib\asm-commons-3.3.jar;C:\work\development\codesearch\serialization\lib\asm-tree-3.3.jar;tostring-all.jar;tostring-agent.jar -javaagent:tostring-agent.jar org.simpleframework.xml.benchmark.asm.Run
*/
public class Run
{
    public static void main(String[] args) {
        Name name = new Name("Dennis", "Michael", "Sosnoski");
        Address address = new Address("1234 5th St.", "Redmond", "WA", "98052");
        Customer customer = new Customer(12345, name, address,
            "425 555-1212", "425 555-1213");
        System.out.println(customer);
    }
}