package simple.xml.stream;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

final class NodeBuffer  {   
   
   private StringBuilder buffer;
   
   private Writer result;
   
   public NodeBuffer(Writer result) {
      this.result = new BufferedWriter(result);
      this.buffer = new StringBuilder();
   }
   
   public void append(String text) {
      buffer.append(text);
   }
   
   public void append(char[] text) {
      append(text, 0, text.length);
   }
   
   public void append(char[] text, int off, int len) {
      buffer.append(text);      
   }
   
   public void append(char text) {
      buffer.append(text);
   }
   
   public void write(String text) throws IOException {
      if(buffer.length() > 0) {
         result.append(buffer);
         reset();
      }
      result.write(text);
   }
   
   public void write(char[] text) throws IOException {
      append(text, 0, text.length);
   }
   
   public void write(char[] text, int off, int len) throws IOException {
      if(buffer.length() > 0) {
         result.append(buffer);
         reset();
      }
      result.write(text);      
   }
   
   public void write(char text) throws IOException {
      if(buffer.length() > 0) {
         result.append(buffer);
         reset();
      }
      result.write(text);
   }
   
   public void flush() throws IOException {
      result.append(buffer);
      result.flush();
      reset();
   }
   
   public void reset() {
      buffer.setLength(0);
   }  
}
 