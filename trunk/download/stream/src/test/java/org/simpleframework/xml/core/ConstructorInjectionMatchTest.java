package org.simpleframework.xml.core;

import java.io.StringWriter;

import junit.framework.TestCase;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Serializer;

/**
 * Created by IntelliJ IDEA.
 * User: e03229
 * Date: 10/11/10
 * Time: 10:52
 * To change this template use File | Settings | File Templates.
 */
public class ConstructorInjectionMatchTest extends TestCase {

    @Root(name = "root")
    private static class RootElement {

        @Element(name = "one")
        private final SimpleElementOne one;

        public RootElement(@Element(name = "one") SimpleElementOne one) {
            this.one = one;
        }
    }

    private static class SimpleElementOne {
        @Element(name = "two")
        private final SimpleElementTwo two;

        public SimpleElementOne(@Element(name = "two") SimpleElementTwo two) {
            this.two = two;
        }
        
        public SimpleElementOne(SimpleElementTwo two, int length) {
        	this.two = two;
        }
    }

    private static class SimpleElementTwo {
        @Attribute(name = "value")
        private final String value;

        public SimpleElementTwo(@Attribute(name = "value") String value) {
            this.value = value;
        }

    }

    /**
     * The stack trace is :
     *  org.simpleframework.xml.core.ConstructorException: No match found for field 'two' private final thirdparty.simplexml.ConstructorInjectionTest$SimpleElementTwo thirdparty.simplexml.ConstructorInjectionTest$SimpleElementOne.two in class thirdparty.simplexml.ConstructorInjectionTest$SimpleElementOne
     *  at org.simpleframework.xml.core.Scanner.validateConstructor(Scanner.java:513)
     *  at org.simpleframework.xml.core.Scanner.validateElements(Scanner.java:452)
     *  at org.simpleframework.xml.core.Scanner.validate(Scanner.java:413)
     *  at org.simpleframework.xml.core.Scanner.scan(Scanner.java:397)
     *  at org.simpleframework.xml.core.Scanner.<init>(Scanner.java:120)
     *  at org.simpleframework.xml.core.ScannerFactory.getInstance(ScannerFactory.java:65)
     *  [...]
     *  at thirdparty.simplexml.ConstructorInjectionTest.testConstructorInjection(ConstructorInjectionTest.java:78)
     */
    public void testConstructorInjection() throws Exception {
        SimpleElementTwo two = new SimpleElementTwo("val");
        SimpleElementOne one = new SimpleElementOne(two);
        RootElement root = new RootElement(one);

        Serializer serializer = new Persister();
        StringWriter output = new StringWriter();
        serializer.write(root, output);
        System.out.println(output.toString());
        serializer.read(RootElement.class, output.toString());
    }
}
