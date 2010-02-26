package org.simpleframework.xml.stream;

import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

class DocumentProvider {
   
   public EventReader provide(InputStream stream) throws Exception {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      InputSource source = new InputSource(stream);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();       
      Document document = builder.parse(source);
      return new DocumentReader(document);   
   }
   
   public EventReader provide(Reader stream) throws Exception {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      builderFactory.setNamespaceAware(true);
      InputSource source = new InputSource(stream);
      DocumentBuilder builder = builderFactory.newDocumentBuilder();       
      Document document = builder.parse(source);
      return new DocumentReader(document);
   }
}
