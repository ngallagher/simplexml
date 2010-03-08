package com.rbsfm.plugin.build.rpc;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
public class RpcRequestHandlerTest implements RequestCallback{
   @Test
   public void post() throws Exception{
      RpcRequestHandler handler=new RpcRequestHandler();
      handler.publish(this,"egpricing","ceemea","2009WK52","12","niall.gallagher@rbs.com");
   }
   public void onError(Request request,Throwable exception){
      exception.printStackTrace();
   }
   public void onResponseReceived(Request request,Response response){
      assertEquals(response.getStatusCode(), 200);
   }
}
