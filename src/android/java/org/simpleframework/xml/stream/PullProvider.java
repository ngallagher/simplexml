package org.simpleframework.xml.stream;

import java.io.InputStream;
import java.io.Reader;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class PullProvider {
   
   public EventReader provide(InputStream stream) throws Exception {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance(null, null);
      XmlPullParser parser = factory.newPullParser();
      parser.setInput(stream, "UTF-8");
      return new PullReader(parser);  
   }
   
   public EventReader provide(Reader stream) throws Exception {
      XmlPullParserFactory factory = XmlPullParserFactory.newInstance(null, null);
      XmlPullParser parser = factory.newPullParser();
      parser.setInput(stream);
      return new PullReader(parser);
   }
}
