import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TemplateProcessor {
   
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
   
   private static String removeIndentation(String text) {
      List<String> lines = LineStripper.stripLines(text);
      int minimum = 10000;
      String indent = "";
      Pattern pattern = Pattern.compile("(\\s*).*");
      for(String line : lines) {
         Matcher matcher = pattern.matcher(line);
         if(matcher.matches()) {
            if(line.matches(".*\\w+.*")) { // only conider real code lines
               String indentText = matcher.group(1);
               int length = indentText.length();
               if(length < minimum) {
                  minimum = length;
                  indent = indentText;
               }
            }
         }
      }
      StringBuilder builder = new StringBuilder();
      for(String line : lines) {
         if(line.indexOf(indent) != -1) {
            line = line.substring(minimum);
         }
         builder.append(line);
         builder.append("\r\n");
      }
      return builder.toString();
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
      try {
         return sourceFile.replace("${"+variableName+"}", text);
      }catch(Exception e) {
         throw new RuntimeException(variableName, e);
      }
   }
   
   private static String escapeHtml(String text) {
      return text.replaceAll(">", "&gt;").replaceAll("<", "&lt;");
   }
   
   //  java TemplateProcessor ./template.xml ./examples/. 
   public static void main(String[] list) throws Exception {
      if(list.length != 3) {
         System.out.println("Incorrect arguments, length=["+list.length+"]");
         System.exit(-1);
      }
      final File baseDir = new File(list[0]);
      final File wholeFileTemplate = new File(list[1]);
      final File outputResultFile = new File(list[2]);
      final File[] fileList = baseDir.listFiles(new ExampleDirFilter());
      final SortedSet<File> sortedFileSet = new TreeSet<File>(new ExampleDirComparator());
      for(File file : fileList) {
         if(file.isDirectory()) {
            sortedFileSet.add(file);
         }
      }
      System.out.println("Sorted as:"+sortedFileSet);
      final List<String> exampleHtmlList = new ArrayList<String>();
      final String wholeFileTemplateText = slurpFile(wholeFileTemplate);
      for(File file : sortedFileSet) {
         if(file.isDirectory()) {
            String fileName = file.getName();
            String javaFileName = "E" + fileName.substring(1);
            File templateFile = new File(file,fileName + ".template.html");
            if(templateFile.exists()) {
               final String templateSource = slurpFile(templateFile);
               final File sourceFile = new File(file, javaFileName + ".java");
               final File xmlFile = new File(file, fileName + ".xml");
               final File outputFile = new File(file, fileName + ".html");
               final FileWriter outputWriter = new FileWriter(outputFile);
               final String xmlSource = slurpFile(xmlFile);
               final String javaSource = fixJava(slurpFile(sourceFile));
               final String javaSnippet = removeIndentation(extractSnippet(javaSource));
   
               String html = replaceVariable(templateSource, escapeHtml(xmlSource), "xmlSnippet");
               html = replaceVariable(html, escapeHtml(javaSnippet), "javaSnippet");
               html = replaceVariable(html, fileName+"/"+fileName+".zip", "downloadLink");
               
               outputWriter.write(html);
               outputWriter.close();
               exampleHtmlList.add(html);
            } else {
               System.out.println("No template file exists for "+templateFile.getCanonicalPath());
            }
         }
      }
      
      StringBuilder fullContent =  new StringBuilder();
      for(String html : exampleHtmlList) {
         fullContent.append(html);
      }
      String fullFile = replaceVariable(wholeFileTemplateText, fullContent.toString(), "content");
      FileWriter w = new FileWriter(outputResultFile);
      w.write(fullFile);
      w.close();
   }
   private static class ExampleDirFilter implements FilenameFilter {

      public boolean accept(File dir, String name) {
         return name.matches(".*example\\d+") && dir.isDirectory();
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
   
   private static class ExampleDirComparator implements Comparator<File> {

      public int compare(File o1, File o2) {
         String name1 = o1.getName();
         String name2 = o2.getName();
         Pattern extractDigits = Pattern.compile("example(\\d+)");
         Matcher matcher1 = extractDigits.matcher(name1);
         if(matcher1.matches()) {
            Matcher matcher2 = extractDigits.matcher(name2);
            if(matcher2.matches()) {
               Integer int1 = Integer.parseInt(matcher1.group(1));
               Integer int2 = Integer.parseInt(matcher2.group(1));
               return int1.compareTo(int2);
            }
         }
         return name1.compareTo(name2);
      }
      
   }
}
