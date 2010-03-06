package com.rbsfm.plugin.build.repository;

/**
 * This is the location that describes where in the repository a
 * resource is located. This provides the root of the file and the
 * path relative to the root of the repository.
 * 
 * @author Niall Gallagher
 */
public class Location {
   public final String path;
   public final String root;
   public Location(String path, String root) {
      this.path = path;
      this.root = root;
   }
}
