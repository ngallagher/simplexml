package com.rbsfm.plugin.build.rpc;
import java.util.ArrayList;
import java.util.List;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
public class RpcRequestHandler extends RpcRequestBuilder implements RequestHandler{
   public void assemble(RequestCallback callback,String projectName,String tag,String installName,String environments,String mail) throws Exception{
      String data = buildAssemble(projectName,tag,installName,Environment.parse(environments), mail);
      create("http://lonms04037.fm.rbsgrp.net:59009/ModulePublicationServer/ModulePublicationService").setRequestData(data).setCallback(callback).finish().send();
   }
   public void publish(RequestCallback callback,String moduleName,String branch,String revision,String branchRevision,String mail) throws Exception{
      String data = buildPublish(moduleName,branch,revision,branchRevision,mail);
      create("http://lonms04037.fm.rbsgrp.net:59009/ModulePublicationServer/ModulePublicationService").setRequestData(data).setCallback(callback).finish().send();
   }
   protected RequestBuilder doCreate(String target){
      RequestBuilder builder=new RequestBuilder(RequestBuilder.POST,target);
      builder.setHeader("Connection","close");
      builder.setHeader("Host","localhost:9999");
      builder.setHeader("User-Agent","Mozilla/5.0 (Windows; U; Windows NT 5.1; en-GB; rv:1.9.1.8) Gecko/20100202 Firefox/3.5.8");
      builder.setHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,");
      builder.setHeader("Accept-Language","en-gb,en;q=0.5");
      builder.setHeader("Accept-Encoding","gzip,deflate");
      builder.setHeader("Accept-Charset","ISO-8859-1,utf-8;q=0.7,*;q=0.7");
      builder.setHeader("Content-Type","text/x-gwt-rpc; charset=utf-8");
      builder.setHeader("Referer","http://localhost:9999/ProjectAssemblyServer/C35FAD9739E7C3D9CDB7FE7B7BD68DCD.cache.html");
      builder.setHeader("Pragma","no-cache");
      builder.setHeader("Cache-Control","no-cache");
      return builder;
   }
   private String buildPublish(String moduleName,String branch,String revision,String branchRevision,String mail) throws Exception{
      return String.format("5|0|9|http://localhost:9999/ModulePublicationServer/|4D2BB86C9B694708FD0D5834CE6A80B1|com.rbsfm.fi.modulepublicationserver.client.ModulePublicationService|publish|com.rbsfm.fi.modulepublicationserver.client.ModulePublicationData|com.rbsfm.fi.modulepublicationserver.client.ModulePublicationDataImpl/2938065861|%s|%s-%s-%s|%s|1|2|3|4|1|5|6|0|7|8|0|0|9|0|0|0|0|0|",moduleName,revision,branch,branchRevision,mail);
   }
   private String buildAssemble(String projectName,String tag,String installName,List<Environment> envs,String mailAddress){
      int size=envs.size();
      String start=String.format("5|0|%s|http://localhost:9999/ProjectAssemblyServer/|50F473AF07CA2557CCF4AE2C5D50CA40|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyService|assemble|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyData|com.rbsfm.fi.projectassemblyserver.client.ProjectAssemblyDataImpl/739800790|java.util.ArrayList/3821976829|java.lang.String/2004016611",13+size);
      for(int i=0;i<envs.size();i++){
         start+=String.format("|%s",envs.get(i).name().toLowerCase());
      }
      start+=String.format("|%s|%s|%s|svn|%s",installName,projectName,tag,mailAddress);
      start+="|1|2|3|4|1|5|6|7";
      start+=String.format("|%s",size);
      for(int i=0;i<envs.size();i++){
         start+=String.format("|8|%s",9+i);
      }
      return start+String.format("|%s|%s|%s|%s|0|%s|0|0|0|0|0|",9+size,10+size,11+size,12+size,13+size);
   }
   private static enum Environment {
      PROD, UAT, DEV, CONT, SUPPORTDEV;
      public static List<Environment> parse(String envs){
         String[] array=envs.split("\\s*,\\s*");
         List<Environment> list=new ArrayList<Environment>();
         for(int i=0;i<array.length;i++){
            list.add(valueOf(array[i].toUpperCase()));
         }
         return list;
      }
   }
}
