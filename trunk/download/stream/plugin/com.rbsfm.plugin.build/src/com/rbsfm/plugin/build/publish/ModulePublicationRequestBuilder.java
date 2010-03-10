package com.rbsfm.plugin.build.publish;
import java.util.Map;
import com.rbsfm.plugin.build.rpc.Method;
import com.rbsfm.plugin.build.rpc.Request;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
public class ModulePublicationRequestBuilder implements RequestBuilder{
   private final String moduleName;
   private final String revision;
   private final String branch;
   private final String branchRevision;
   private final String mailAddress;
   public ModulePublicationRequestBuilder(String moduleName,String branch,String revision,String branchRevision,String mailAddress) {
      this.moduleName = moduleName;
      this.branch = branch;
      this.revision = revision;
      this.branchRevision = branchRevision;
      this.mailAddress = mailAddress;
   }   
   public void address(StringBuilder builder){
      builder.append("http://lonms04037.fm.rbsgrp.net:59009/ModulePublicationServer/ModulePublicationService");
   }
   public void header(Map<String,String> header){
      header.put("Connection","close");
      header.put("Host","localhost:9999");
      header.put("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.1.8) Gecko/20100202 Firefox/3.5.8");
      header.put("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,");
      header.put("Accept-Language","en-gb,en;q=0.5");
      header.put("Accept-Encoding","gzip,deflate");
      header.put("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
      header.put("Content-Type","text/x-gwt-rpc; charset=utf-8");
      header.put("Referer","http://localhost:9999/ModulePublicationServer/C35FAD9739E7C3D9CDB7FE7B7BD68DCD.cache.html");
      header.put("Pragma","no-cache");
      header.put("Cache-Control","no-cache");
   }
   public void body(StringBuilder builder){
      builder.append(5);
      builder.append("|").append(0);
      builder.append("|").append(9);
      builder.append("|").append("http://localhost:9999/ModulePublicationServer/");
      builder.append("|").append("4D2BB86C9B694708FD0D5834CE6A80B1");
      builder.append("|").append("com.rbsfm.fi.modulepublicationserver.client.ModulePublicationService");
      builder.append("|").append("publish");
      builder.append("|").append("com.rbsfm.fi.modulepublicationserver.client.ModulePublicationData");
      builder.append("|").append("com.rbsfm.fi.modulepublicationserver.client.ModulePublicationDataImpl/2938065861");
      builder.append("|").append(moduleName);
      builder.append("|").append(revision).append("-").append(branch).append("-").append(branchRevision);
      builder.append("|").append(mailAddress);
      builder.append("|").append(1);
      builder.append("|").append(2);
      builder.append("|").append(3);
      builder.append("|").append(4);
      builder.append("|").append(1);
      builder.append("|").append(5);      
      builder.append("|").append(6);
      builder.append("|").append(0);
      builder.append("|").append(7);
      builder.append("|").append(8);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(9);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|");
   }
   public static void main(String[] list) throws Exception{
      RequestBuilder builder = new ModulePublicationRequestBuilder(list[0],list[1],list[2],list[3],list[4]);
      Request request = new Request(builder,null);
      request.execute(Method.POST);
   }
}
