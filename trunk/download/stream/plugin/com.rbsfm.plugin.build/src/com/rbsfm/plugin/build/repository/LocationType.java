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
   private LocationType(String type){
      this.type = type;
   }
}
