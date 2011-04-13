package org.simpleframework.xml.stream;

import java.io.InputStream;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
 
class PullProvider implements Provider {
   
   private final XmlPullParserFactory factory;
   
   public PullProvider() throws Exception {
      this.factory = XmlPullParserFactory.newInstance();
      this.factory.setNamespaceAware(true);
   }
   
   public EventReader provide(InputStream source) throws Exception {
      XmlPullParser parser = factory.newPullParser();
      
      if(source != null) {
         parser.setInput(source, null);
      }
      return new PullReader(parser);  
   }
   
   public EventReader provide(Reader source) throws Exception {
      XmlPullParser parser = factory.newPullParser();
      
      if(source != null) {
         parser.setInput(source);
      }
      return new PullReader(parser);
   }
}
