package com.rbsfm.plugin.build.assemble;
import java.util.List;
import java.util.Map;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
public class ProjectAssemblyRequestBuilder implements RequestBuilder{
   private final List<String> environmentList;
   private final String projectName;
   private final String installName;
   private final String tagName;
   private final String mailAddress;
   public ProjectAssemblyRequestBuilder(String projectName, String installName, String tagName, List<String> environmentList, String mailAddress) {
      this.environmentList = environmentList;
      this.projectName = projectName;
      this.installName = installName;
      this.tagName = tagName;
      this.mailAddress = mailAddress;
   }   
   public void address(StringBuilder builder){
      builder.append("http://lonms04037.fm.rbsgrp.net:59009/ProjectAssemblyServer/ProjectAssemblyService");
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
      header.put("Referer","http://localhost:9999/ProjectAssemblyServer/C35FAD9739E7C3D9CDB7FE7B7BD68DCD.cache.html");
      header.put("Pragma","no-cache");
      header.put("Cache-Control","no-cache");
   }
   public void body(StringBuilder builder){
      int size = environmentList.size();
      
      builder.append(5);
      builder.append("|").append("0");
      builder.append("|").append(13 + size);
      builder.append("|").append("http://localhost:9999/ProjectAssemblyServer/");
      builder.append("|").append("50F473AF07CA2557CCF4AE2C5D50CA40");
      builder.append("|").append("com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyService");
      builder.append("|").append("assemble|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyData");
      builder.append("|").append("com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyDataImpl/739800790");
      builder.append("|").append("java.util.ArrayList/3821976829");
      builder.append("|").append("java.lang.String/2004016611");
      
      for(String environment : environmentList){
         builder.append("|").append(environment);
      }
      builder.append("|").append(installName);
      builder.append("|").append(projectName);
      builder.append("|").append(tagName);
      builder.append("|").append(mailAddress);
      builder.append("|").append(1);
      builder.append("|").append(2);
      builder.append("|").append(3);
      builder.append("|").append(4);
      builder.append("|").append(1);
      builder.append("|").append(5);
      builder.append("|").append(6);
      builder.append("|").append("7");
      builder.append("|").append(size);
      
      for(int i = 0; i < size; i++){
         builder.append("|").append(8);
         builder.append("|").append(9 + i);
      }
      builder.append("|").append(9 + size);
      builder.append("|").append(10 + size);
      builder.append("|").append(11 + size);
      builder.append("|").append(12 + size);
      builder.append("|").append(0);
      builder.append("|").append(12 + size);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
   }
}
