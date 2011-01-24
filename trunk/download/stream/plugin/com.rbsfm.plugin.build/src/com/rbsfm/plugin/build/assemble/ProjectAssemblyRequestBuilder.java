package com.rbsfm.plugin.build.assemble;
import java.util.Map;
import java.util.prefs.Preferences;
import com.rbsfm.plugin.build.rpc.Method;
import com.rbsfm.plugin.build.rpc.Request;
import com.rbsfm.plugin.build.rpc.RequestBuilder;
public class ProjectAssemblyRequestBuilder implements RequestBuilder{
   private final String[] environmentList;
   private final String projectName;
   private final String installName;
   private final String tagName;
   private final String mailAddress;
   public ProjectAssemblyRequestBuilder(String projectName,String installName,String tagName,String[] environmentList,String mailAddress){
      this.environmentList = environmentList;
      this.projectName = projectName;
      this.installName = installName;
      this.tagName = tagName;
      this.mailAddress = mailAddress;
   }
   public void address(StringBuilder builder){
     Preferences preferences = Preferences.userNodeForPackage(ProjectAssemblyRequestBuilder.class);
     String server = preferences.get("server", "http://lonms06619.fm.rbsgrp.net:59009/ProjectAssemblyServer/ProjectAssemblyService");
     builder.append(server);     
   }
   public void header(Map<String,String> header){
      header.put("Accept", "*/*");
      header.put("Accept-Language", "en-us");
      header.put("Referer", "http://sydmw12385:9999/ProjectAssemblyServer/91CD486DABE691C54B93D2B7C8C37AF5.cache.html");
      header.put("Content-Type", "text/x-gwt-rpc; charset=utf-8");
      header.put("UA-CPU", "x86");
      header.put("Accept-Encoding", "gzip, deflate");
      header.put("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
      header.put("Host", "sydmw12385:9999");
      //header.put("Content-Length", "474");
      header.put("Connection", "close");
      header.put("Cache-Control", "no-cache");
      header.put("Cookie", "projectassemblyserver.user.email=niall.gallagher@rbs.com; proxy=http://lonms06619.fm.rbsgrp.net:59009/ProjectAssemblyServer/ProjectAssemblyServer.html");
      /*header.put("Connection", "close");
      header.put("Host", "localhost:9999");
      header.put("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.1.8) Gecko/20100202 Firefox/3.5.8");
      header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,");
      header.put("Accept-Language", "en-gb,en;q=0.5");
      header.put("Accept-Encoding", "gzip,deflate");
      header.put("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
      header.put("Content-Type", "text/x-gwt-rpc; charset=utf-8");
      header.put("Referer", "http://localhost:9999/ProjectAssemblyServer/C35FAD9739E7C3D9CDB7FE7B7BD68DCD.cache.html");
      header.put("Pragma", "no-cache");
      header.put("Cache-Control", "no-cache");*/
   }
   public void body(StringBuilder builder){
     //"5|0|14|http://localhost:7777 /ProjectAssemblyServer/|C679019429F34E9A7E91EB954F5164D7|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyService|assemble|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyData|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyDataImpl/739800790|java.util.ArrayList/3821976829|java.lang.String/2004016611|dev|uat|aussie-2010WK48-rmdsbridge-1|aussie_rmdsbridge|                             svn|niall.gallagher@rbs.com|1|2|3|4|1|5|6|7|2|8|9|8|10|11|12|11|13|0|14|0|0|0|0|0|"
     //"5|0|15|http://sydmw12385:9999/ProjectAssemblyServer/|C679019429F34E9A7E91EB954F5164D7|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyService|assemble|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyData|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyDataImpl/739800790|java.util.ArrayList/3821976829|java.lang.String/2004016611|uat|dev|aussie-2010WK48-rmdsbridge-1|aussie_rmdsbridge|aussie-2010WK48-rmdsbridge-1|svn|niall.gallagher@rbs.com|1|2|3|4|1|5|6|7|2|8|9|8|10|11|12|13|14|0|15|0|0|0|0|0|
      int size = environmentList.length;
      builder.append(5);
      builder.append("|").append("0");
      builder.append("|").append(13 + size);
      //builder.append("|").append("http://localhost:9999/ProjectAssemblyServer/");
      builder.append("|").append("http://sydmw12385:9999/ProjectAssemblyServer/");
      //builder.append("|").append("50F473AF07CA2557CCF4AE2C5D50CA40");
      builder.append("|").append("C679019429F34E9A7E91EB954F5164D7");
      builder.append("|").append("com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyService");
      builder.append("|").append("assemble");
      builder.append("|").append("com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyData");
      builder.append("|").append("com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyDataImpl/739800790");
      builder.append("|").append("java.util.ArrayList/3821976829");
      builder.append("|").append("java.lang.String/2004016611");
      for(String environment : environmentList){
         builder.append("|").append(environment);
      }
      builder.append("|").append(installName);
      builder.append("|").append(projectName);
      builder.append("|").append(tagName);
      builder.append("|").append("svn");
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
      builder.append("|").append(13 + size);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|").append(0);
      builder.append("|");
   }
   public static void main(String[] list) throws Exception{
      RequestBuilder builder = new ProjectAssemblyRequestBuilder(list[0], list[1], list[2], list[3].split(","), list[4]);
      Request request = new Request(builder, null, false);
      request.execute(Method.POST);
   }
}
