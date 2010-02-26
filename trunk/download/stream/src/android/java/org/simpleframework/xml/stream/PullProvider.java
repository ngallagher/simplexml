package org.simpleframework.xml.stream;

import java.io.InputStream;
import java.io.Reader;

import org.xmlpull.mxp1.MXParser;
import org.xmlpull.v1.XmlPullParser;

public class PullProvider {
   
   public EventReader provide(InputStream stream) throws Exception {
      XmlPullParser parser = new MXParser();
      parser.setInput(stream, "UTF-8");
      return new PullReader(parser);  
   }
   
   public EventReader provide(Reader stream) throws Exception {
      XmlPullParser parser = new MXParser();
      parser.setInput(stream);
      return new PullReader(parser);
   }
}
