package org.simpleframework.xml.stream;

import java.io.InputStream;
import java.io.Reader;

public class EventProvider {

   public static EventReader provide(InputStream stream) throws Exception {
      try {
         Class.forName("org.xmlpull.v1.XmlPullParserFactory");
         return new PullProvider().provide(stream);
      } catch(Throwable e) {
         e.printStackTrace();
         return new DocumentProvider().provide(stream);
      }
   }
   
   public static EventReader provide(Reader stream) throws Exception {
      try {
         Class.forName("org.xmlpull.v1.XmlPullParserFactory");
         return new PullProvider().provide(stream);
      } catch(Throwable e) {
         e.printStackTrace();
         return new DocumentProvider().provide(stream);
      }
   }
}
