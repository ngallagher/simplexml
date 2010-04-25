#region Using directives
using System;
#endregion
namespace SimpleFramework.Xml.Strategy {
   class PackageParser {
      private const String scheme = "http://";
      public Class Revert(String reference) {
         URI uri = new URI(reference);
         String domain = uri.getHost();
         String path = uri.getPath();
         String[] list = domain.split("\\.");
         if(list.length > 1) {
            domain = list[1] + "." + list[0];
         } else {
            domain = list[0];
         }
         String type =  domain + path.replaceAll("\\/+", ".");
         return Class.forName(type);
      }
      public String Parse(String className) {
         return new Convert(className).FastParse();
      }
      public String Parse(Class type) {
         return new Convert(type.getName()).FastParse();
      }
      public static class Convert {
         private char[] array;
         private int count;
         private int mark;
         private int size;
         private int pos;
         public Convert(String type) {
            this.array = type.toCharArray();
         }
         public String FastParse() {
            char[] work = new char[array.length + 10];
            Scheme(work);
            Domain(work);
            Path(work);
            return new String(work, 0, pos);
         }
         public void Scheme(char[] work) {
            "http://".getChars(0, 7, work, 0);
            pos += 7;
         }
         public void Path(char[] work) {
            for(int i = size; i < array.length; i++) {
               if(array[i] == '.') {
                  work[pos++] = '/';
               } else {
                  work[pos++] = array[i];
               }
            }
         }
         public void Domain(char[] work) {
            while(size < array.length) {
               if(array[size] == '.') {
                  if(count++ == 1) {
                     break;
                  }
                  mark = size + 1;
               }
               size++;
            }
            for(int i = 0; i < size - mark; i++) {
               work[pos++] = array[mark + i];
            }
            work[pos++] = '.';
            work[size + 7] = '/';
            for(int i = 0; i < mark - 1; i++) {
               work[pos++] = array[i];
            }
         }
      }
   }
}
