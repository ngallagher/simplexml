#region Using directives
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class Replace : LineStripper {
      private const String LGPL =
         " \\* This library is free software.*02111-1307\\s+USA\\s+\\*\\/";
      private const String APACHE =
         " * Licensed under the Apache License, Version 2.0 (the \"License\");\n"+
         " * you may not use this file except in compliance with the License.\n"+
         " * You may obtain a copy of the License at\n"+
         " *\n"+
         " *     http://www.apache.org/licenses/LICENSE-2.0\n"+
         " *\n"+
         " * Unless required by applicable law or agreed to in writing, software\n"+
         " * distributed under the License is distributed on an \"AS IS\" BASIS,\n"+
         " * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or \n"+
         " * implied. See the License for the specific language governing \n"+
         " * permissions and limitations under the License.\n"+
         " */";
      public void Main(String[] list) {
         List<File> files = GetFiles(new File(list[0]));
         Pattern pattern = Pattern.compile(LGPL, Pattern.DOTALL | Pattern.MULTILINE);
         for(File file : files) {
            String text = GetFile(file);
            text = pattern.matcher(text).replaceAll(APACHE);
            Save(file, text);
         }
      }
      public void Save(File file, String text) {
         OutputStream out = new FileOutputStream(file);
         OutputStreamWriter utf = new OutputStreamWriter(out, "UTF-8");
         utf.write(text);
         utf.flush();
         utf.close();
         out.flush();
         out.close();
      }
      public String GetFile(File file) {
         InputStream in = new FileInputStream(file);
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         byte[] block = new byte[8192];
         int count = 0;
         while((count = in.read(block)) != -1) {
            out.write(block, 0, count);
         }
         return out.toString("UTF-8");
      }
      public List<File> GetFiles(File root) {
         List<File> files = new ArrayList<File>();
         File[] fileList = root.listFiles();
         for(File file : fileList) {
            if(file.isDirectory() && !file.getName().equals(".svn")) {
               files.addAll(GetFiles(file));
            } else if(file.getName().endsWith(".java")){
               files.add(file);
            }
         }
         return files;
      }
   }
}
