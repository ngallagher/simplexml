package org.simpleframework.xml.benchmark;

import java.io.OutputStream;

public class NullOutputStream extends OutputStream {

   public void write(int octet) {}
   public void write(byte[] chunk) {}
   public void write(byte[] chunk, int off, int len) {}
   public void flush() {}
   public void close() {}
   
}
