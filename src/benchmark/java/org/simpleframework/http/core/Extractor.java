package org.simpleframework.http.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.simpleframework.util.buffer.ArrayBuffer;
import org.simpleframework.util.buffer.Buffer;

public class Extractor {
   
   private static final byte[] LINE = new byte[] {13,10};
   private static final byte[] REQUEST = new byte[] {13,10,13,10};
   
   public String extractStatus(InputStream in) throws IOException {
      Buffer status = new ArrayBuffer(255, 1024);
      int octet = 0;
      int pos = 0;

      while((octet = in.read()) != -1) {
         if(octet != LINE[pos++]) {
            pos = 0;
         }
         if(pos == LINE.length) {
            break;
         }
         status.append(new byte[]{(byte)octet});
      }
      return status.encode("ISO-8859-1").trim();      
   }
   
   
   public Map<String, String> extractHeader(InputStream in) throws IOException {
      Map<String, String> map = new HashMap<String, String>();
      Buffer header = new ArrayBuffer(1024, 8192);
      int octet = 0;
      int pos = 0;

      while((octet = in.read()) != -1) {
         if(octet != REQUEST[pos++]) {
            pos = 0;
         }
         if(pos == REQUEST.length) {
            break;
         }
         header.append(new byte[]{(byte)octet});
      }     
      String text = header.encode("ISO-8859-1");
      String[] lines = text.split("\r\n");
      
      for(String line : lines) {
         if(line.indexOf(':') > 0) {
            String[] pair = line.trim().split("\\s*:\\s*");
            map.put(pair[0], pair[1]);
         }
      }
      return map;      
   }
}
