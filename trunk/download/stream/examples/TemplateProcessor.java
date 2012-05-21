import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;


public class TemplateProcessor {

   @Root
   private static class Template {
      @Element
      private String title;
      @Element
      private String introText;
      @Element(required=false)
      private String separatorText;
   }
   
   private static String slurpFile(File file) throws Exception {
      FileInputStream in = new FileInputStream(file);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      int octet = 0;
      while((octet = in.read())!= -1){
         out.write(octet);
      }
      return out.toString("UTF-8");
   }
   
   private static String extractSnippet(String sourceFile) {
      Pattern pattern = Pattern.compile(".*\\/\\*\\s*snippet\\s*\\*\\/(.*)\\/\\*\\s*snippet\\s*\\*\\/.*", Pattern.DOTALL | Pattern.MULTILINE);
      Matcher matcher = pattern.matcher(sourceFile);
      if(matcher.matches()) {
         return matcher.group(1);
      } else {
         pattern = Pattern.compile(".*\\/\\/\\s*snippet\\s*(.*)\\/\\/\\s*snippet\\s*.*", Pattern.DOTALL | Pattern.MULTILINE);
         matcher = pattern.matcher(sourceFile);
         if(matcher.matches()) {
            return matcher.group(1);
         }
      }
      return "<font color='#ff0000'><i>Snippet not found</i></font>";
   }
   
   private static String fixJava(String text) {
      return text.replaceAll("public static class", "public class").replaceAll("private static class", "public class");
   }
   
   private static String indentText(String text, String indent) {
      List<String> lines = LineStripper.stripLines(text);
      StringBuilder builder = new StringBuilder();
      builder.append(indent);
      builder.append("\r\n");
      for(String line : lines) {
         builder.append(indent);
         builder.append(line);
         builder.append("\r\n");
      }
      return builder.toString();
   }
   
   private static String replaceVariable(String sourceFile, String text, String variableName) {
      return sourceFile.replaceAll("\\$\\{"+variableName+"\\}", text);
   }
   
   private static String escapeHtml(String text) {
      return text.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
   }
   
   //  java TemplateProcessor ./template.xml ./examples/. 
   public static void main(String[] list) throws Exception {
      if(list.length != 4) {
         System.out.println("Incorrect arguments, length=["+list.length+"]");
         System.exit(-1);
      }
      final File templatePath = new File(list[0]);
      final File baseDir = new File(list[1]);
      final File resultFile = new File(list[2]);
      final File outputResultFile = new File(list[3]);
      System.out.println("Processing with template=["+templatePath.getCanonicalPath()+"] in directory=["+baseDir.getCanonicalPath()+"]");
      final File[] fileList = baseDir.listFiles(new ExampleDirFilter());
      final Persister persister = new Persister();
      final String templateSource = slurpFile(templatePath);
      List<String> exampleHtmlList = new ArrayList<String>();
      String resultFileText = slurpFile(resultFile);
      for(File file : fileList) {
         if(file.isDirectory()) {
            String fileName = file.getName();
            String javaFileName = "E" + fileName.substring(1);
            File descFile = new File(file,fileName + ".desc.xml");
            if(descFile.exists()) {
               Template template = persister.read(Template.class, descFile);
               File sourceFile = new File(file, javaFileName + ".java");
               File xmlFile = new File(file, fileName + ".xml");
               File outputFile = new File(file, fileName + ".html");
               FileWriter outputWriter = new FileWriter(outputFile);
               String xmlSource = indentText(slurpFile(xmlFile), "   ");
               String javaSource = fixJava(slurpFile(sourceFile));
               String javaSnippet = extractSnippet(javaSource);
   
               String html = replaceVariable(templateSource, escapeHtml(xmlSource), "xmlSnippet");
               html = replaceVariable(html, escapeHtml(javaSnippet), "javaSnippet");
               html = replaceVariable(html, template.introText.trim(), "introText");
               html = replaceVariable(html, template.title.trim(), "title");
               if(template.separatorText == null) {
                  template.separatorText = "";
               }
               html = replaceVariable(html, template.separatorText.trim(), "separatorText");
               html = replaceVariable(html, fileName+"/"+fileName+".zip", "downloadLink");
               
               outputWriter.write(html);
               outputWriter.close();
               exampleHtmlList.add(html);
            } else {
               System.out.println("No desc file exists for "+descFile.getCanonicalPath());
            }
         }
      }
      
      StringBuilder fullContent =  new StringBuilder();
      for(String html : exampleHtmlList) {
         fullContent.append(html);
      }
      resultFileText = replaceVariable(resultFileText, fullContent.toString(), "content");
      FileWriter w = new FileWriter(outputResultFile);
      w.write(resultFileText);
      w.close();
   }
   private static class ExampleDirFilter implements FilenameFilter {

      public boolean accept(File dir, String name) {
         return name.matches(".*example\\d+");
      }
      
   }

   public static class LineStripper {

      public static List<String> stripLines(InputStream text) throws IOException {
         ByteArrayOutputStream out = new ByteArrayOutputStream();
         byte[] chunk = new byte[8192];
         int count = 0;

         while ((count = text.read(chunk)) != -1) {
            out.write(chunk, 0, count);
         }
         text.close();
         return stripLines(out.toString("UTF-8"));
      }

      public static List<String> stripLines(String text) {
         List<Token> list = new LinkedList<Token>();
         char[] array = text.toCharArray();
         int start = 0;
         
         while(start < array.length) {
            Token line = nextLine(array, start);
            list.add(line);
            start += line.length;
         }
         List<String> lines = new ArrayList<String>(list.size() + 1);
         for(Token token : list) {
            lines.add(token.toString());
         }
         return lines;
      }
      
      private static Token nextLine(char[] text, int startFrom) {
         for(int i = startFrom; i < text.length; i++) {
            if(text[i] == '\r') {
               if(i + 1 < text.length && text[i + 1] == '\n') {
                  i++;
               }
               return new Token(text, startFrom, ++i - startFrom);
            }
            if(text[i] == '\n') {
               return new Token(text, startFrom, ++i - startFrom);
            }
         }
         return new Token(text, startFrom, text.length - startFrom);
      }
      
      private static class Token {
         private final char[] source;   
         private final int start;
         private final int length;
         public Token(char[] source, int start, int length) {
            this.source = source;
            this.start = start;
            this.length = length;
         }
         @Override
         public String toString() {
            for(int i = length - 1; i >= 0; i--) {
               if(!Character.isWhitespace(source[start + i])) {
                  return new String(source, start, i + 1);
               }
            }
            return "";
         }
      }
   }
}
