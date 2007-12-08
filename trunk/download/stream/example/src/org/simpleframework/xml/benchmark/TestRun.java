package org.simpleframework.xml.benchmark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(name="test")
public class TestRun {

   @Attribute(name="id")
   private String id;
   
   @Element(name="executorClass")
   private String executor;
   
   @Element(name="schemaClass")
   private String type;
   
   @Element(name="sourceFile")
   private String source;
   
   @Element(name="iterations")
   private int count;
   
   private byte[] content;
   
   public String getId() {
      return id;
   }
   
   public int getIterations() {
      return count;
   }

   public Class getExecutorClass() throws ClassNotFoundException {
      return Class.forName(executor);
   }
   
   public OutputStream getResultStream() throws IOException {
      return new NullOutputStream();
   }
   
   public InputStream getSourceStream() throws IOException {
      return new ByteArrayInputStream(getSourceBytes());
   }
   
   public byte[] getSourceBytes() throws IOException {
      if(content == null) {
         File file = getSource();
         InputStream source = new FileInputStream(file);
         ByteArrayOutputStream buffer = new ByteArrayOutputStream();
         byte[] chunk = new byte[2048];
         int size = 0;
      
         while((size = source.read(chunk)) != -1) {
            buffer.write(chunk, 0, size);
         }  
         content = buffer.toByteArray();
      }
      return content;
   }

   public File getSource() throws IOException {
      File file = new File(source);
      
      if(!file.exists()) {
         throw new FileNotFoundException(source);
      }
      return file;
   }

   public Class getSchemaClass() throws ClassNotFoundException {
      return Class.forName(type);
   }
}
