#region Using directives
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class LineStripper {
      public List<String> StripLines(InputStream text) {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         byte[] chunk = new byte[8192];
         int count = 0;
         while ((count = text.read(chunk)) != -1) {
            out.write(chunk, 0, count);
         }
         text.close();
         return StripLines(out.toString("UTF-8"));
      }
      public List<String> StripLines(String text) {
         List<String> list = new ArrayList<String>();
         char[] array = text.toCharArray();
         int start = 0;
         while(start < array.length) {
            String line = NextLine(array, start);
            String trimLine = TrimLine(line);
            list.add(trimLine);
            start += line.length();
         }
         return list;
      }
      public String TrimLine(String line) {
         for(int i = line.length() - 1; i >= 0; i--) {
            if(!Character.isWhitespace(line.charAt(i))) {
               return line.substring(0, i + 1);
            }
         }
         return line;
      }
      public String NextLine(char[] text, int startFrom) {
        /* for(int i = startFrom; i < text.length; i++) {
            if(text[i] == '\r' || text[i] == '\n') {
               while(i < text.length && (text[i] == '\r' || text[i] == '\n')) {
                  i++;
               }
               return new String(text, startFrom, i - startFrom);
            }
         }
         */
         for(int i = startFrom; i < text.length; i++) {
            if(text[i] == '\r') {
               if(i < text.length && text[i] == '\n') {
                  i++;
               }
               return new String(text, startFrom, i - startFrom);
            }
            if(text[i] == '\n') {
               return new String(text, startFrom, ++i - startFrom);
            }
         }
         return new String(text, startFrom, text.length - startFrom);
      }
   }
}
