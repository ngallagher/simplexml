package org.simpleframework.xml;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.custommonkey.xmlunit.XMLTestCase;
import org.simpleframework.xml.graph.CycleStrategy;
import org.simpleframework.xml.load.Persister;
import org.simpleframework.xml.load.Strategy;
import org.simpleframework.xml.stream.CamelCaseStyle;
import org.simpleframework.xml.stream.Format;
import org.simpleframework.xml.stream.HyphenStyle;
import org.simpleframework.xml.stream.Style;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class ValidationTestCase extends XMLTestCase {

   private static TransformerFactory transformerFactory;
  
	private static Transformer transformer;   
   
   private static DocumentBuilderFactory builderFactory;

   private static DocumentBuilder builder;
        
   static  {
      try {           
         builderFactory = DocumentBuilderFactory.newInstance();
         builder = builderFactory.newDocumentBuilder();

         transformerFactory = TransformerFactory.newInstance();
         transformer = transformerFactory.newTransformer();
      } catch(Exception cause) {
         cause.printStackTrace();              
      }         
   }

   private static File directory;

   static {
        try {
            String path = System.getProperty("output");                                       
            directory = new File(path);
        } catch(Exception cause){
            directory = new File("output");
        }         
        if(!directory.exists()) {
            directory.mkdirs();                
        }
   }

   public void testDirectory() throws Exception {
      assertTrue(directory.exists());           
   }

   public static synchronized void validate(Serializer out, Object type) throws Exception {
      validate(type, out);
   }
    public static synchronized void validate(Object type, Serializer out) throws Exception {
        File destination = new File(directory, type.getClass().getSimpleName() + ".xml");
        OutputStream file = new FileOutputStream(destination);
        StringWriter buffer = new StringWriter();
        out.write(type, buffer);
        String text = buffer.toString();
        byte[] octets = text.getBytes("UTF-8");
        System.out.write(octets);
        System.out.flush();
        file.write(octets);
        file.close();        
        validate(text);
        
        File domDestination = new File(directory, type.getClass().getSimpleName() + ".dom.xml");
        File asciiDestination = new File(directory, type.getClass().getSimpleName() + ".ascii-dom.xml");
        OutputStream domFile = new FileOutputStream(domDestination);
        OutputStream asciiFile = new FileOutputStream(asciiDestination);
        Writer asciiOut = new OutputStreamWriter(asciiFile, "iso-8859-1");
        Document doc = builder.parse(new InputSource(new StringReader(text)));   
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(new DOMSource(doc), new StreamResult(domFile));
        transformer.transform(new DOMSource(doc), new StreamResult(asciiOut));    
        
        domFile.close();      
        asciiFile.close();
        
        out.validate(type.getClass(), text);
       
        File hyphenFile = new File(directory, type.getClass().getSimpleName() + ".hyphen.xml");
        Strategy strategy = new CycleStrategy();
        Style style = new HyphenStyle();
        Format format = new Format(style);
        Persister hyphen = new Persister(strategy, format);
        
        hyphen.write(type, hyphenFile);
        hyphen.write(type, System.out);
        
        File camelCaseFile = new File(directory, type.getClass().getSimpleName() + ".camel-case.xml");
        Style camelCaseStyle = new CamelCaseStyle(true, false);
        Format camelCaseFormat = new Format(camelCaseStyle);
        Persister camelCase = new Persister(strategy, camelCaseFormat);
        
        camelCase.write(type, camelCaseFile);
        camelCase.write(type, System.out);
    }

    public static synchronized void validate(String text) throws Exception {    
        builder.parse(new InputSource(new StringReader(text)));   
    }
}
