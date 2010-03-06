package com.rbsfm.plugin.build.repository;

/**
 * This defines the locations a resource can be within the repository
 * so that it can be determined the relative path of the resource.
 * 
 * @author Niall Gallagher
 */
public enum LocationType {
   TAGS("tags"),
   BRANCHES("branches"),
   TRUNK("trunk");
   public final String type;
   public final int size;
   private LocationType(String type){
      this.size = type.length();
      this.type = type;
   }
   public static Location parse(String location) throws Exception {
      for(LocationType type : values()) {
         int index = location.indexOf(type.type);
         int offset = type.size;
         if(index != -1){
            String path = location.substring(index + offset);
            String root = location.substring(0, index + offset);
            return new Location(path, root);
         }
      }
      throw new Exception("Invalid location");
   }
}
