package com.rbsfm.plugin.build.svn;
enum LocationType {
   TAGS("tags"), BRANCHES("branches"), TRUNK("trunk");
   public final String type;
   public final int size;
   private LocationType(String type){
      this.size=type.length();
      this.type=type;
   }
   public static LocationType type(String location) throws RepositoryException{
      Scheme.scheme(location);
      for(LocationType type:values()){
         int index=location.indexOf(type.type);
         if(index!=-1){
            return type;
         }
      }
      throw new RepositoryException("Invalid location %s",location);
   }
   public static Location parse(String location) throws RepositoryException{
      Scheme.scheme(location);
      for(LocationType type:values()){
         int index=location.indexOf(type.type);
         int offset=type.size;
         if(index!=-1){
            String path=location.substring(index+offset);
            String root=location.substring(0,index+offset);
            String prefix="";
            if(type!=TRUNK){
               int token=path.indexOf('/',1);
               if(token!=-1){
                  prefix=path.substring(1,token);
                  path=path.substring(token);
               }
            }
            return new Location(path,root,prefix);
         }
      }
      throw new RepositoryException("Invalid location %s",location);
   }
   public static Copy copy(String location,String prefix,LocationType type) throws RepositoryException{
      LocationType current=type(location);
      Location from=parse(location);
      String root=from.root;
      if(type!=TRUNK){
         root=root.replaceAll(current.type,type.type);
      }
      Location to=new Location(from.path,root,prefix);
      return new Copy(from,to);
   }
}
