package com.rbsfm.plugin.build.rpc;
import java.util.ArrayList;
import java.util.List;
public class SocketRpcHandler implements RpcHandler{
   private final SocketClient client;
   public SocketRpcHandler(String host,int port) throws Exception{
      this.client=new SocketClient(host,port);
   }
   public void publish(String moduleName,String branch,String revision,String branchRevision,String mail) throws Exception{
      client.post("/ModulePublicationServer/ModulePublicationService",String.format("5|0|9|http://localhost:9999/ModulePublicationServer/|4D2BB86C9B694708FD0D5834CE6A80B1|com.rbsfm.fi.modulepublicationserver.client.ModulePublicationService|publish|com.rbsfm.fi.modulepublicationserver.client.ModulePublicationData|com.rbsfm.fi.modulepublicationserver.client.ModulePublicationDataImpl/2938065861|%s|%s-%s-%s|%s|1|2|3|4|1|5|6|0|7|8|0|0|9|0|0|0|0|0|",moduleName,revision,branch,branchRevision,mail));
   }
   public void assemble(String projectName,String tag,String installName,String envs,String mail) throws Exception{
      client.post("/ProjectAssemblyServer/ProjectAssemblyService",buildRpcForModulePublication(projectName,tag,installName,Environment.parse(envs),mail));
   }
   private String buildRpcForModulePublication(String projectName,String tag,String installName,List<Environment> envs,String mailAddress){
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
