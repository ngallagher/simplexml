package com.rbsfm.plugin.build.rpc;
import java.util.Map;
public interface RequestBuilder {
   public void address(StringBuilder builder);
   public void header(Map<String,String> header);
   public void body(StringBuilder builder);
}