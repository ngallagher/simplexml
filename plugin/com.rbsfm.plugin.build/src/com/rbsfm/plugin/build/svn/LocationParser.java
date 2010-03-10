package com.rbsfm.plugin.build.svn;
class LocationParser{
   public static Parent parent(String location) throws RepositoryException{
      Scheme.scheme(location);
      for(Parent type : Parent.values()){
         int index = location.indexOf(type.type);
         if(index != -1){
            return type;
         }
      }
      throw new RepositoryException("Invalid location %s", location);
   }
   public static Location parse(String location) throws RepositoryException{
      Scheme.scheme(location);
      for(Parent type : Parent.values()){
         int index = location.indexOf(type.type);
         int offset = type.size;
         if(index != -1){
            String path = location.substring(index + offset);
            String root = location.substring(0, index + offset);
            String prefix = "";
            if(type != Parent.TRUNK){
               int token = path.indexOf('/', 1);
               if(token != -1){
                  prefix = path.substring(1, token);
                  path = path.substring(token);
               }
            }
            return new Location(path, root, prefix);
         }
      }
      throw new RepositoryException("Invalid location %s", location);
   }
   public static Copy copy(String location, String prefix, Parent type) throws RepositoryException{
      return copy(parse(location), prefix, type);
   }
   public static Copy copy(Location location, String prefix, Parent type) throws RepositoryException{
      String absolute = location.getAbsolutePath();
      Parent parent = parent(absolute);
      String root = location.root;
      if(type != Parent.TRUNK){
         root = root.replaceAll(parent.type, type.type);
      }
      Location to = new Location(location.path, root, prefix);
      return new Copy(location, to);
   }
}
