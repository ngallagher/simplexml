package com.rbsfm.plugin.build.rpc;
import java.io.Writer;
import java.util.Map;
public interface RequestBuilder {
   public void build(StringBuilder address,Map<String,String> header,Writer body)throws Exception;
}