package org.simpleframework.http.core;

import java.net.InetSocketAddress;

import org.simpleframework.util.buffer.Buffer;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;
import org.simpleframework.xml.core.Commit;

@Root
public class ConnectionTask {
   
   @Element
   private RequestTask request;
   
   @Attribute
   private String location;
   
   @Attribute
   private int port;
   
   @Attribute
   private int repeat;
   
   @Transient
   private byte[] pipeline;
   
   @Transient
   private InetSocketAddress address;
   
   @Commit
   private void commit() throws Exception {
      address = new InetSocketAddress(location, port);
      pipeline = request.getRequest(repeat);
   }
   
   public Buffer execute(Client client) throws Exception {
      return client.execute(address, pipeline);
   }
}
