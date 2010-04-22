package org.simpleframework.xml.util;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageConverter extends Replace {

   private static final List<Class<? extends ConversionPhase>> CONVERTERS = new ArrayList<Class<? extends ConversionPhase>>();
   private static final Map<String, String> NAMESPACE = new LinkedHashMap<String, String>();
   private static final Map<String, String> USING = new LinkedHashMap<String, String>();
   private static final String INDENT = "   ";

   static {
      USING.put("import org.simpleframework.xml.convert.*","using SimpleFramework.Xml.Util;");
      USING.put("import org.simpleframework.xml.filter.*", "using SimpleFramework.Xml.Filter;");
      USING.put("import org.simpleframework.xml.strategy.*", "using SimpleFramework.Xml.Strategy;");
      USING.put("import org.simpleframework.xml.core.*","using SimpleFramework.Xml.Core;");
      USING.put("import org.simpleframework.xml.util.*", "using SimpleFramework.Xml.Util;");
      USING.put("import org.simpleframework.xml.stream.*","using SimpleFramework.Xml.Stream;");
      USING.put("import org.simpleframework.xml.*","using SimpleFramework.Xml;");
   }
   
   static {
      NAMESPACE.put("package org.simpleframework.xml;","namespace SimpleFramework.Xml {");
      NAMESPACE.put("package org.simpleframework.xml.convert;","namespace SimpleFramework.Xml.Util {");
      NAMESPACE.put("package org.simpleframework.xml.filter;", "namespace SimpleFramework.Xml.Filter {");
      NAMESPACE.put("package org.simpleframework.xml.strategy;", "namespace SimpleFramework.Xml.Strategy {");
      NAMESPACE.put("package org.simpleframework.xml.core;","namespace SimpleFramework.Xml.Core {");
      NAMESPACE.put("package org.simpleframework.xml.util;", "namespace SimpleFramework.Xml.Util {");
      NAMESPACE.put("package org.simpleframework.xml.stream;","namespace SimpleFramework.Xml.Stream {");
   }
   
   static {
      CONVERTERS.add(PopulateUsing.class);
      CONVERTERS.add(AddUsing.class);
      CONVERTERS.add(StripImports.class);
      CONVERTERS.add(CreateNamespace.class);
      CONVERTERS.add(ReplaceComments.class);
      CONVERTERS.add(ReplaceDocumentation.class);
      CONVERTERS.add(ReplacePatterns.class);
      CONVERTERS.add(ReplaceConventions.class);
      CONVERTERS.add(StripCrap.class);
   }

   public static void main(String list[]) throws Exception {
      List<File> files = getFiles(new File(list[0]));
      for(File file : files) {
         SourceDetails details = new SourceDetails();
         String text = getFile(file);
         for(Class<? extends ConversionPhase> phaseType : CONVERTERS) {
            Constructor<? extends ConversionPhase> factory = phaseType.getDeclaredConstructor();
            if(!factory.isAccessible()) {
               factory.setAccessible(true);
            }
            ConversionPhase phase = factory.newInstance();
            text = phase.convert(text, details);
         }
         save(new File(file.getCanonicalPath().replaceAll("\\.java", ".cs")), text);
      }
   }
   
   private static class SourceDetails {
      
      private Set<String> using = new HashSet<String>();
      public Set<String> getUsing() {
         return using;
      }
      public void addUsing(String usingValue) {
         using.add(usingValue);
      }
   }
   
   private static interface ConversionPhase {     
      public String convert(String source, SourceDetails details) throws Exception;
   }
   
   private static class PopulateUsing implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         for(String line : lines) {
            if(line.matches("^import.*")) {
               line = line.trim();
               for(String importName : USING.keySet()) {
                  if(line.matches(importName)) {
                     importName = USING.get(importName);
                     details.addUsing(importName);
                     break;
                  }
               }
            }
         }
         return source;
      }
   }
   
   private static class AddUsing implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         boolean importsDone = false;       
         for(String line : lines) {
            if(!importsDone) {
               if(line.matches("^package.*")) {
                  for(String using : details.getUsing()) {
                     writer.append("\n");
                     writer.append(using);
                     writer.append("\n");
                  }
                  writer.append("\n \n");
                  importsDone = true;
               }
            }
            writer.append(line);
            writer.append("\n");
         }
         return writer.toString();
      }
   }
   
   private static class StripImports implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();      
         for(String line : lines) {
            if(!line.matches("^import.*")) {
               writer.append(line);
               writer.append("\n");
            }
         }
         return writer.toString();
      }
   }
   
   private static class CreateNamespace implements ConversionPhase {      
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         String indent = "";
         for(String line : lines) {
            line = line.replaceAll("\\s*$", "");
            writer.append(indent);
            if(indent == null || indent.equals("")){
               for(String packageName : NAMESPACE.keySet()) {
                  if(line.matches(packageName + ".*")) {
                     indent = INDENT;
                     line = NAMESPACE.get(packageName);
                     break;
                  }
               }
            }
            writer.append(line);
            writer.append("\n");           
         }
         writer.append("}");
         return writer.toString();
      }
   }
   
   private static class ReplaceConventions implements ConversionPhase {
      private static final List<String> MODIFIERS = new ArrayList<String>();
      static {
         MODIFIERS.add("public static");
         MODIFIERS.add("private static");
         MODIFIERS.add("protected static");
         MODIFIERS.add("public");
         MODIFIERS.add("private");
         MODIFIERS.add("protected");;
      }
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         main: for(String line : lines) {
            for(String modifier : MODIFIERS) {
               Pattern methodMatch = Pattern.compile("^(\\s*)"+modifier+"\\s+([a-zA-Z]+)\\s+([a-z])([a-zA-Z]+)\\((.+)");
               Matcher matcher = methodMatch.matcher(line);
               if(matcher.matches()) {
                  String indent = matcher.group(1);
                  String type = matcher.group(2);
                  String start = matcher.group(3);
                  String remainder = matcher.group(4);
                  String signature = matcher.group(5);
                  writer.append(indent);
                  writer.append("public ");
                  writer.append(type);
                  writer.append(" ");
                  writer.append(start.toUpperCase());
                  writer.append(remainder);
                  writer.append("(");
                  writer.append(signature);
                  writer.append("\n");
                  continue main;
               }
            }
            writer.append(line);
            writer.append("\n");
         }
         return writer.toString();
      }
   }
  
   private static class StripCrap implements ConversionPhase {
      private static final List<String> TOKENS = new ArrayList<String>();      
      static {
         TOKENS.add("^\\s*\\/\\/\\/\\s*$");
         TOKENS.add("^\\s*$");
         TOKENS.add("^\\s+.*@author.*$");
      }
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         main: for(String line : lines) {
            for(String token : TOKENS) {
               if(token.startsWith("^") && line.matches(token)) {
                  continue main;
               }
            }
            writer.append(line);
            writer.append("\n");
         }
         return writer.toString();
      }
   }
   
   private static class ReplacePatterns implements ConversionPhase {
      private static final Map<String, String> TOKENS = new LinkedHashMap<String, String>();      
      static {
         TOKENS.put("static final", "const");
         TOKENS.put("final", "readonly");
         TOKENS.put("final class", "sealed class");
         TOKENS.put("boolean", "bool");
         TOKENS.put("implements", ":");
         TOKENS.put("extends", ":");
         TOKENS.put("\\)\\s*throws\\s.*\\{", ") {");
         TOKENS.put("\\)\\s*throws\\s.*;", ");");
         TOKENS.put("org.simpleframework.xml.convert","SimpleFramework.Xml.Util");
         TOKENS.put("org.simpleframework.xml.filter", "SimpleFramework.Xml.Filter");
         TOKENS.put("org.simpleframework.xml.strategy", "SimpleFramework.Xml.Strategy");
         TOKENS.put("org.simpleframework.xml.core","SimpleFramework.Xml.Core");
         TOKENS.put("org.simpleframework.xml.util", "SimpleFramework.Xml.Util");
         TOKENS.put("org.simpleframework.xml.stream","SimpleFramework.Xml.Stream");
         TOKENS.put("org.simpleframework.xml","SimpleFramework.Xml");
      }
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         for(String line : lines) {
            for(String token : TOKENS.keySet()) {
               String value = TOKENS.get(token);
               if(line.startsWith("^") && line.matches(token)) {
                  line = line.replaceAll(token, value);
               } else if(line.matches(".*" +token+".*")) {
                  line = line.replaceAll(token, value);
               }
            }
            writer.append(line);
            writer.append("\n");
         }
         return writer.toString();
      }
   }
   
   private static class ReplaceDocumentation implements ConversionPhase {
      private static final Map<String, String> TOKENS = new HashMap<String, String>();
      static {
         TOKENS.put("return", "returns");
         TOKENS.put("see", "seealso");
      }
      public String convert(String source, SourceDetails details) throws Exception {
         Pattern paramComment = Pattern.compile("^(\\s*)///.*@param\\s*([a-zA-Z]*)\\s*(.*)$");
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         main: for(String line : lines) {
            Matcher paramMatcher = paramComment.matcher(line);
            if(paramMatcher.matches()) {
               String indent = paramMatcher.group(1);
               String name = paramMatcher.group(2);
               String description = paramMatcher.group(3);
               writer.append(indent);
               writer.append("/// <param name=\"");
               writer.append(name);
               writer.append("\">\n");
               writer.append(indent);
               writer.append("/// ");
               writer.append(description);
               writer.append("\n");
               writer.append(indent);
               writer.append("/// </param>\n");
            } else {
               for(String token : TOKENS.keySet()) {
                  Pattern tokenComment = Pattern.compile("^(\\s*)///.*@"+token+"\\s(.*)$");
                  Matcher tokenMatcher = tokenComment.matcher(line);
                  if(tokenMatcher.matches()) {
                     String replace = TOKENS.get(token);
                     String indent = tokenMatcher.group(1);
                     String description = tokenMatcher.group(2);
                     writer.append(indent);
                     writer.append("/// <"+replace+">\n");
                     writer.append(indent);
                     writer.append("/// ");
                     writer.append(description);
                     writer.append("\n");
                     writer.append(indent);
                     writer.append("/// </"+replace+">\n");
                     continue main;
                  }
               }
               writer.append(line);
               writer.append("\n");
            }
         }
         return writer.toString();
      }
   }
   
   private static class ReplaceComments implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         Iterator<String> iterator = lines.iterator();
         StringWriter writer = new StringWriter();
         while(iterator.hasNext()) {
            String line = iterator.next();
            if(line.matches("\\s*\\/\\*\\*")) {
               comment(iterator, writer, line);
            } else {
               writer.append(line);
               writer.append("\n");
            }
         }
         return writer.toString();
      }
      private void comment(Iterator<String> lines, StringWriter writer, String line) throws Exception {
         Pattern normalComment = Pattern.compile("^(\\s*)\\*.*$");
         Pattern parameterComment = Pattern.compile("^(\\s*)\\*.*@.*$");
         boolean endSummary = false;
         writer.append(line.replaceAll("\\/\\*\\*", "///<summary>"));
         writer.append("\n");
         while(lines.hasNext()) {
            String nextLine = lines.next();
            nextLine = nextLine.substring(1);
            if(nextLine.matches("^\\s*\\*\\/")) {
               if(!endSummary) {
                  writer.append(nextLine.replaceAll("\\*\\/", "///</summary>"));
               } else {
                  writer.append(nextLine.replaceAll("\\*\\/", "///"));
               }
               writer.append("\n");
               return;
            }
            if(!endSummary) {
               Matcher parameterMatch = parameterComment.matcher(nextLine);
               if(parameterMatch.matches()) {
                  writer.append(parameterMatch.group(1));
                  writer.append("///</summary>");
                  writer.append("\n");
                  writer.append(parameterMatch.group(1));
                  writer.append(nextLine.replaceAll("^\\s*\\*", "///"));
                  writer.append("\n");
                  endSummary = true;
               } else {
                  Matcher normalMatch = normalComment.matcher(nextLine);
                  if(normalMatch.matches()) {
                     writer.append(normalMatch.group(1));
                     writer.append(nextLine.replaceAll("^\\s*\\*", "///"));
                     writer.append("\n");
                  }
               }
            } else {
               Matcher normalMatch = normalComment.matcher(nextLine);
               if(normalMatch.matches()) {
                  writer.append(normalMatch.group(1));
                  writer.append(nextLine.replaceAll("^\\s*\\*", "///"));
                  writer.append("\n");
               }else {
                  throw new IllegalStateException("Comment does not end well " + nextLine);
               }
            }  
         }
      }
   }

}
