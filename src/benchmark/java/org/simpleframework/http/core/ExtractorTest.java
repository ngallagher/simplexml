package org.simpleframework.http.core;

import java.io.InputStream;
import java.util.Map;

import junit.framework.TestCase;

import org.simpleframework.util.buffer.ArrayBuffer;
import org.simpleframework.util.buffer.Buffer;

public class ExtractorTest extends TestCase {
   
   private static final String HEADER = 
   "GET http://www.google.com/ HTTP/1.1\r\n"+
   "Content-Type: text/plain\r\n"+
   "User-Agent: Mozilla/1.1\r\n"+
   "Content-Length: 10\r\n"+
   "Connection: keep-alive\r\n"+
   "\r\n"+
   "0123456789";
   
   public void testExtractor() throws Exception {
      Extractor extractor = new Extractor();
      Buffer buffer = new ArrayBuffer(1024);      
      buffer.append(HEADER.getBytes("ISO-8859-1"));
      InputStream stream = buffer.getInputStream();
      String status = extractor.extractStatus(stream);
      Map<String, String> header = extractor.extractHeader(stream);
      
      assertEquals(status, "GET http://www.google.com/ HTTP/1.1");
      assertEquals(header.get("Content-Type"), "text/plain");
      assertEquals(header.get("User-Agent"), "Mozilla/1.1");
      assertEquals(header.get("Content-Length"), "10");
      assertEquals(header.get("Connection"), "keep-alive");      
   }

}
