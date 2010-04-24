package org.simpleframework.xml.util;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageConverter extends Replace {

   private static final List<Class<? extends ConversionPhase>> STAGE_ONE = new ArrayList<Class<? extends ConversionPhase>>();
   private static final List<Class<? extends SubstitutionPhase>> STAGE_TWO = new ArrayList<Class<? extends SubstitutionPhase>>();
   private static final Map<String, String> NAMESPACE = new LinkedHashMap<String, String>();
   private static final Map<String, String> USING = new LinkedHashMap<String, String>();
   private static final Map<String, String> FILES = new LinkedHashMap<String, String>();
   private static final String INDENT = "   ";
   
   static {
      //FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml");
      FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\core", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Core");
      FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\filter", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Filter");
      FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\strategy", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Strategy");
      FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\stream", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Stream");
      FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\convert", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Convert");
      FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\transform", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Transform");
      FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\util", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Util");
   }

   static {
      USING.put("import org.simpleframework.xml.convert.*","using SimpleFramework.Xml.Util;");
      USING.put("import org.simpleframework.xml.filter.*", "using SimpleFramework.Xml.Filter;");
      USING.put("import org.simpleframework.xml.strategy.*", "using SimpleFramework.Xml.Strategy;");
      USING.put("import org.simpleframework.xml.core.*","using SimpleFramework.Xml.Core;");
      USING.put("import org.simpleframework.xml.util.*", "using SimpleFramework.Xml.Util;");
      USING.put("import org.simpleframework.xml.stream.*","using SimpleFramework.Xml.Stream;");
      USING.put("import org.simpleframework.xml.*","using SimpleFramework.Xml;");
      USING.put("import java.util.*","using System.Collections.Generic;");
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
      STAGE_ONE.add(CanonicalizeFile.class);
      STAGE_ONE.add(DefineType.class);
      STAGE_ONE.add(PopulateUsing.class);
      STAGE_ONE.add(AddUsing.class);
      STAGE_ONE.add(StripImports.class);
      STAGE_ONE.add(CreateNamespace.class);
      STAGE_ONE.add(GetFields.class);
      STAGE_ONE.add(ReplaceComments.class);
      STAGE_ONE.add(ReplaceDocumentation.class);
      STAGE_ONE.add(ReplaceKeyWords.class);
      STAGE_ONE.add(ReplaceMethodConventions.class);
      STAGE_ONE.add(StripCrap.class);
      STAGE_ONE.add(ReplaceLicense.class);
      STAGE_ONE.add(SubstituteAnnotations.class);
      STAGE_ONE.add(ConvertAnnotationAttributes.class);
      STAGE_ONE.add(SetAnnotationAttributes.class);
      STAGE_ONE.add(ConvertClassBeanMethods.class);
   }
   
   static {
      STAGE_TWO.add(SubstituteMethods.class);
   }

   public static void main(String list[]) throws Exception {
      SourceProject project = new SourceProject();
      for(String from : FILES.keySet()) {
         List<File> files = getFiles(new File(from));
         for(File file : files) {
            SourceDetails details = new SourceDetails(file);
            String text = getFile(file);
            details.setText(text);
            for(Class<? extends ConversionPhase> phaseType : STAGE_ONE) {
               Constructor<? extends ConversionPhase> factory = phaseType.getDeclaredConstructor();
               if(!factory.isAccessible()) {
                  factory.setAccessible(true);
               }
               ConversionPhase phase = factory.newInstance();
               details.setText(phase.convert(details.getText(), details));
            }
            project.addSource(details);
         }
      }
      for(SourceDetails details : project.getDetails()) {
         for(Class<? extends SubstitutionPhase> phaseType : STAGE_TWO) {
            Constructor<? extends SubstitutionPhase> factory = phaseType.getDeclaredConstructor();
            if(!factory.isAccessible()) {
               factory.setAccessible(true);
            }
            SubstitutionPhase phase = factory.newInstance();
            details.setText(phase.convert(details.getText(), details, project));
         }
      }
      List<String> newFiles = new ArrayList<String>();
      for(SourceDetails details : project.getDetails()) {
         File saveAs = new File(details.getSource().getCanonicalPath().replaceAll("\\.java", ".cs"));
         save(saveAs, details.getText());
         newFiles.add(saveAs.getCanonicalPath().replaceAll("^.*src", "src"));
      }
      for(String entry : newFiles) {
         System.out.println(" <Compile Include=\""+entry+"\"/>");
      }
   }
   
   private static String convertMethod(String originalMethod) {
      if(originalMethod != null && !Character.isUpperCase(originalMethod.charAt(0))){
         StringBuilder builder = new StringBuilder(originalMethod.length());
         char first = originalMethod.charAt(0);
         builder.append(Character.toUpperCase(first));
         builder.append(originalMethod.substring(1));
         return builder.toString();
      }
      return originalMethod;
   }
   
   private static enum SourceType {
      ANNOTATION,
      INTERFACE,
      CLASS,
      ENUM
   }
   
   private static class SourceDetails {     
      private Set<String> using = new TreeSet<String>();
      private Map<String, String> attributes = new LinkedHashMap<String, String>();
      private Map<String, String> fields = new LinkedHashMap<String, String>();
      private List<String> imports = new ArrayList<String>();
      private List<String> methods = new ArrayList<String>();
      private String packageName;
      private SourceType type;
      private String name;
      private File source;
      private String text;
      public SourceDetails(File source) {
         this.using.add("using System;");
         this.source = source;
      }
      public String getText() {
         return text;
      }
      public void setText(String text) {
         this.text = text;
      }
      public File getSource() {
         return source;
      }
      public String getName() {
         return name;
      }
      public void setName(String name) {
         this.name = name;
      }
      public String getPackage() {
         return packageName;
      }
      public void setPackage(String packageName) {
         this.packageName = packageName;
      }
      public String getFullyQualifiedName() {
         return String.format("%s.%s", packageName, name);
      }
      public SourceType getType() {
         return type;
      }
      public void setType(SourceType type) {
         this.type = type;
      }
      public Set<String> getUsing() {
         return using;
      }
      public void addMethod(String method) {
         methods.add(method);
      }
      public List<String> getMethods() {
         return methods;
      }
      public void addImport(String importClass) {
         imports.add(importClass);
      }
      public List<String> getImports() {
         return imports;
      }
      public Map<String, String> getAttributes() {
         return attributes;
      }
      public void addAttribute(String type, String attribute) {
         attributes.put(attribute, type);
      }
      public void addUsing(String usingValue) {
         using.add(usingValue);
      }
      public Map<String, String> getFields() {
         return fields;
      }
      public void addField(String name, String type) {
         fields.put(name, type);
      }
   }
   
   private static class SourceProject {
      private Map<String, List<SourceDetails>> packages = new HashMap<String, List<SourceDetails>>();
      private Map<String, SourceDetails> names = new HashMap<String, SourceDetails>();
      private List<SourceDetails> all = new ArrayList<SourceDetails>();
      public List<SourceDetails> getDetails() {
         return all;
      }
      public void addSource(SourceDetails details) {
         String packageName = details.getPackage();
         String name = details.getName();
         List<SourceDetails> packageFiles = packages.get(packageName);
         if(packageFiles == null) {
            packageFiles = new ArrayList<SourceDetails>();
            packages.put(packageName, packageFiles);
         }
         packageFiles.add(details);
         all.add(details);
         names.put(name, details);
      }
      public SourceDetails getDetails(String name) {
         return names.get(name);
      }
   }
   
   private static abstract class SubstitutionPhase {  
      public Map<String, String> calculateSubstututions(SourceDetails details, SourceProject project) throws Exception {
         Map<String, String> substitutes = new HashMap<String, String>();
         for(String field : details.getFields().keySet()) {
            String type = details.getFields().get(field);
            SourceDetails fieldDetails = project.getDetails(type);
            if(fieldDetails != null) {
               populateFrom(fieldDetails, field, substitutes);
            }
         }
         populateFrom(details, null, substitutes);
         return substitutes;      
      }
      private void populateFrom(SourceDetails details, String field, Map<String, String> substitutes) {
         List<String> methods = details.getMethods();
         for(String originalMethod : details.getMethods()) {
            substitutes.put(originalMethod+"\\(", convertMethod(originalMethod)+"\\(");
         }
         if(field != null && !field.equals("")) {
            for(String originalMethod : methods) {
               String originalToken = String.format("%s.%s\\(", field, originalMethod); 
               String token = String.format("%s.%s\\(", field, convertMethod(originalMethod)); // create the substitute                  
               substitutes.put(originalToken, token);
            }
         }
      }
      public abstract String convert(String source, SourceDetails details, SourceProject project) throws Exception;
   }
   
   private static interface ConversionPhase {     
      public String convert(String source, SourceDetails details) throws Exception;
   }
   
   public static class SubstituteMethods extends SubstitutionPhase {
      public String convert(String source, SourceDetails details, SourceProject project) throws Exception {
         List<String> lines = stripLines(source);
         Map<String, String> substitutions = calculateSubstututions(details, project);
         StringWriter writer = new StringWriter();
         for(String line : lines) {
            for(String substitute : substitutions.keySet()) {
               line = line.replaceAll(substitute, substitutions.get(substitute));
            }
            writer.append(line);
            writer.append("\n");
         }
         return writer.toString();
      }
   }
   
   public static class CanonicalizeFile implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         if(source.indexOf('\t') != -1) {
            throw new Exception("File contains tab "+details.getSource());
         }
         return source.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
      }
   }
   
   public static class GetFields implements ConversionPhase {
      private static final List<String> MODIFIERS = new ArrayList<String>();
      static {
         MODIFIERS.add("private final");
         MODIFIERS.add("private");
         MODIFIERS.add("protected");
         MODIFIERS.add("protected final");
      }
      public String convert(String source, SourceDetails details) throws Exception {
         List<Pattern> patterns = new ArrayList<Pattern>();
         for(String modifier : MODIFIERS) {
            patterns.add(Pattern.compile("^\\s+"+modifier+"\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s*;.*$"));
         }
         List<String> lines = stripLines(source);
         for(String line : lines) {
            for(Pattern pattern : patterns) {
               Matcher matcher = pattern.matcher(line);
               if(matcher.matches()) {
                  String type = matcher.group(1);
                  String name = matcher.group(2);
                  details.addField(name, type);
                  break;
               }
            }
         }
         return source;
      }
   }
   
   public static class ConvertAnnotationAttributes implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         Pattern pattern = Pattern.compile("^(\\s+)public\\s+(.*)\\s+([a-zA-Z]+)\\(\\)\\s+default\\s+.+;.*$");
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         for(String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()) {
               String indent = matcher.group(1);
               String type = matcher.group(2);
               String method = matcher.group(3);   
               String attribute = method.toLowerCase();
               details.addAttribute(type, attribute);
               writer.append(indent);
               writer.append("public ");
               writer.append(type);
               writer.append(" ");
               writer.append(method);
               writer.append(" {\n");
               writer.append(indent);
               writer.append("   get {\n");
               writer.append(indent);
               writer.append("      return ").append(attribute).append(";\n");
               writer.append(indent);
               writer.append("   }\n");
               writer.append(indent);
               writer.append("   set {\n");
               writer.append(indent);
               writer.append("      ").append(attribute).append(" = value;\n");
               writer.append(indent);
               writer.append("   }\n");
               writer.append(indent);
               writer.append("}\n");
            } else {
               writer.append(line);
               writer.append("\n");
            }
         }
         return writer.toString();
      }
   }
   
   private static class ConvertClassBeanMethods implements ConversionPhase {
      private static final Pattern get = Pattern.compile("^(\\s+)public\\s+(.*)\\s+Get([a-zA-Z]+)\\(\\)\\s+\\{.*$");
      private static final Pattern set = Pattern.compile("^(\\s+)public\\s+.*\\s+Set([a-zA-Z]+)\\((.*)\\s+[a-zA-Z]\\)\\s+\\{.*$");
      private static final Pattern rightBrace = Pattern.compile("^.*\\{.*$");
      private static final Pattern leftBrace = Pattern.compile("^.*\\}.*$");
      private class MethodDetail {
         public final String type;
         public final String name;
         public final String indent;
         public final String content;
         public final int lineCount;
         public MethodDetail(String name, String type, String indent, String content, int lineCount) {
            this.name = name;
            this.type = type;
            this.indent = indent;
            this.content = content;
            this.lineCount = lineCount;
         }
      }
      private class Property {
         private MethodDetail get;
         private MethodDetail set;
         private boolean done;
         public boolean isDone() {
            return done;
         }
         public void done() {
            done = true;
         }
         public boolean verify() throws Exception {
            if(get != null && set != null) {
               if(get.name.equals(set.name)) {
                  throw new IllegalStateException("Property names do not match for '"+get.name+"' and '"+set.name+"'");
               }
               if(get.type.equals(set.type)) {
                  throw new IllegalStateException("Property types do not match for '"+get.type+"' and '"+set.type+"'");
               }
            }
            return true;
         }
      }
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         Map<String, Property> properties = new HashMap<String, Property>();
         StringWriter writer = new StringWriter();
         extract(source, details, properties);
         for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            Matcher getter = get.matcher(line);
            if(getter.matches()) {
               String name = getter.group(3);
               Property property = properties.get(name);
               if(!property.isDone()) {
                  write(property.get.indent, property, writer);
               }
               String indent = property.get.indent;
               int indentLength = indent.length();
               for(int j = 0; j <= property.get.lineCount; j++) {
                  line = lines.get(i++);
                  writer.append(indent);
                  writer.append("//");
                  if(line.length() < indentLength) {
                     throw new IllegalStateException("Line '"+line+"' is out of place in " + writer.toString());
                  }
                  writer.append(line.substring(indentLength));
                  writer.append("\n");
               }
            } else {
               Matcher setter = set.matcher(line);
               if(setter.matches()) {
                  String name = setter.group(2);
                  Property property = properties.get(name);
                  if(!property.isDone()) {
                     write(property.set.indent, property, writer);
                  }
                  String indent = property.set.indent;
                  int indentLength = indent.length();
                  for(int j = 0; j <= property.set.lineCount; j++) {
                     line = lines.get(i++);
                     writer.append(indent);
                     writer.append("//");
                     if(line.length() < indentLength) {
                        throw new IllegalStateException("Line '"+line+"' is out of place in " + writer.toString());
                     }
                     writer.append(line.substring(indentLength));
                     writer.append("\n");
                  }
               } else {
                  writer.append(line);
                  writer.append("\n");
               }
            }
         }
         return writer.toString();
      }
      public void write(String indent, Property property, StringWriter writer) throws Exception {
         if(property.verify()) {
            if(property.get != null) {
               writer.append(property.get.indent);
               writer.append("public ");
               writer.append(property.get.type);
               writer.append(" ");
               writer.append(property.get.name);
               writer.append(" {\n");
               writer.append(property.get.indent);
               writer.append("   get {\n");
               List<String> lines = stripLines(property.get.content);
               for(String line : lines) {
                  writer.append("   ");
                  writer.append(line);
                  writer.append("\n");
               }
            }
            if(property.set != null) {
               writer.append(property.set.indent);
               writer.append("public ");
               writer.append(property.set.type);
               writer.append(" ");
               writer.append(property.set.name);
               writer.append(" {\n");
               writer.append(property.set.indent);
               writer.append("   set {\n");
               List<String> lines = stripLines(property.set.content);
               for(String line : lines) {
                  writer.append("   ");
                  writer.append(line);
                  writer.append("\n");
               }
            }
            writer.append(indent);
            writer.append("}");
            writer.append("\n");
         }
         property.done();
      }
      public void extract(String source, SourceDetails details, Map<String, Property> properties) throws Exception {
         List<String> lines = stripLines(source);
         for(int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);        
            Matcher getter = get.matcher(line);
            if(getter.matches()) {
               String indent = getter.group(1);
               String type = getter.group(2);
               String name = getter.group(3);
               StringBuilder writer = new StringBuilder();
               int lineCount = 0;
               int braces = 0;
               i++;
               for(; braces >= 0 && i < lines.size(); i++) {
                  line = lines.get(i);
                  Matcher right = rightBrace.matcher(line);
                  if(right.matches()) {
                     braces++;
                  } else {
                     Matcher left = leftBrace.matcher(line);
                     if(left.matches()) {
                        braces--;
                     }
                  }
                  writer.append(line);
                  writer.append("\n");
                  lineCount++;
               }
               Property property = properties.get(name);
               if(property == null) {
                  property = new Property();
                  properties.put(name, property);
               }
               if(property.get != null) {
                  throw new IllegalStateException("The property '"+name+"' is defined twice in "+details.getFullyQualifiedName());
               }
               property.get = new MethodDetail(name, type, indent, writer.toString(), lineCount);
            } else {
               Matcher setter = set.matcher(line);
               if(setter.matches()) {
                  String indent = setter.group(1);
                  String name = setter.group(3);
                  String type = setter.group(2);
                  StringBuilder writer = new StringBuilder();
                  int lineCount = 0;
                  int braces = 0;
                  i++;
                  for(; braces >= 0 && i < lines.size(); i++) {
                     line = lines.get(i);
                     Matcher right = rightBrace.matcher(line);
                     if(right.matches()) {
                        braces++;
                     } else {
                        Matcher left = leftBrace.matcher(line);
                        if(left.matches()) {
                           braces--;
                        }
                     }
                     writer.append(line);
                     writer.append("\n");
                     lineCount++;
                  }
                  Property property = properties.get(name);
                  if(property == null) {
                     property = new Property();
                     properties.put(name, property);
                  }
                  if(property.get != null) {
                     throw new IllegalStateException("The property '"+name+"' is defined twice in "+details.getFullyQualifiedName());
                  }
                  property.set = new MethodDetail(name, type, indent, writer.toString(), lineCount);
               }
            }
         } 
      }
   }
   
   public static class SetAnnotationAttributes implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         if(details.getType() == SourceType.ANNOTATION) {
            Pattern pattern = Pattern.compile("^(\\s+)public\\s+class\\s+[a-zA-Z]+.*$");
            List<String> lines = stripLines(source);
            StringWriter writer = new StringWriter();
            Map<String, String> attributes = details.getAttributes();
            for(String line : lines) {
               Matcher matcher = pattern.matcher(line);
               if(matcher.matches()) {
                  String indent = matcher.group(1);
                  writer.append(line);
                  writer.append("\n");
                  for(String attribute : attributes.keySet()){
                     String type = attributes.get(attribute);
                     writer.append(indent);
                     writer.append("   private ");
                     writer.append(type);
                     writer.append(" ");
                     writer.append(attribute);
                     writer.append(";\n");                     
                  }
               } else {
                  writer.append(line);
                  writer.append("\n");
               }
            }
            return writer.toString();
         }
         return source;
      }
   }  
   
   private static class DefineType implements ConversionPhase {   
      private static final Map<Pattern, SourceType> PATTERNS = new HashMap<Pattern, SourceType>();
      static {
         PATTERNS.put(Pattern.compile("^public\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
         PATTERNS.put(Pattern.compile("^public\\s+final\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
         PATTERNS.put(Pattern.compile("^class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
         PATTERNS.put(Pattern.compile("^public\\s+abstract\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
         PATTERNS.put(Pattern.compile("^abstract\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
         PATTERNS.put(Pattern.compile("^final\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
         PATTERNS.put(Pattern.compile("^public\\s+@interface\\s+([a-zA-Z]*).*"), SourceType.ANNOTATION);
         PATTERNS.put(Pattern.compile("^@interface\\s+([a-zA-Z]*).*"), SourceType.ANNOTATION);
         PATTERNS.put(Pattern.compile("^public\\s+interface\\s+([a-zA-Z]*).*"), SourceType.INTERFACE);
         PATTERNS.put(Pattern.compile("^interface\\s+([a-zA-Z]*).*"), SourceType.INTERFACE);
         PATTERNS.put(Pattern.compile("^public\\s+enum\\s+([a-zA-Z]*).*"), SourceType.ENUM);
         PATTERNS.put(Pattern.compile("^enum\\s+([a-zA-Z]*).*"), SourceType.ENUM);
      }
      public String convert(String source, SourceDetails details) throws Exception{
         List<String> lines = stripLines(source);
         for(String line : lines) {
            for(Pattern pattern : PATTERNS.keySet()) {
               Matcher matcher = pattern.matcher(line);
               if(matcher.matches()) {
                  SourceType type = PATTERNS.get(pattern);
                  String name = matcher.group(1);
                  details.setType(type);
                  details.setName(name);
                  return source;
               }
            }
         }
         throw new IllegalStateException("File can not be classified " + details.getSource());
      }
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
         Pattern pattern = Pattern.compile("^package\\s+([a-zA-Z\\.]*)\\s*;.*");
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         boolean importsDone = false;       
         for(String line : lines) {
            if(!importsDone) {
               Matcher matcher = pattern.matcher(line);
               if(matcher.matches()) {
                  String packageName = matcher.group(1);
                  writer.append("\n#region Using directives\n");
                  for(String using : details.getUsing()) {
                     writer.append(using);
                     writer.append("\n");
                  }
                  writer.append("\n#endregion\n");
                  details.setPackage(packageName);
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
         Pattern pattern = Pattern.compile("^import\\s+([a-zA-Z\\.]*)\\s*;.*$");
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();      
         for(String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()) {
               String importClass = matcher.group(1);
               details.addImport(importClass);
            } else {
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
   
   private static class ReplaceMethodConventions implements ConversionPhase {
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
               Pattern methodMatch = Pattern.compile("^(\\s*)"+modifier+"\\s+([a-zA-Z\\[\\]\\<\\>]+)\\s+([a-zA-Z]+)\\((.+)");
               Matcher matcher = methodMatch.matcher(line);
               if(matcher.matches()) {
                  String indent = matcher.group(1);
                  String type = matcher.group(2);
                  String originalMethod = matcher.group(3);;
                  String signature = matcher.group(4);
                  String method = convertMethod(originalMethod);
                  writer.append(indent);
                  writer.append("public ");
                  writer.append(type);
                  writer.append(" ");
                  writer.append(method);
                  writer.append("(");
                  writer.append(signature);
                  writer.append("\n");
                  details.addMethod(originalMethod);
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
         TOKENS.add("^\\s+.*@throws.*$");
         TOKENS.add("^\\s+.*@exception.*$");
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
   
   private static class ReplaceKeyWords implements ConversionPhase {
      private static final Map<String, String> TOKENS = new LinkedHashMap<String, String>();      
      static {
         TOKENS.put("<code>", "<c>");
         TOKENS.put("</code>", "</c>");
         TOKENS.put("<pre>", "</code>");
         TOKENS.put("</pre>", "</code>");
         TOKENS.put("\\(List ", "(IList ");
         TOKENS.put(" List ", " IList ");
         TOKENS.put(",List ", ",IList ");
         TOKENS.put("HashMap ", "Dictionary ");
         TOKENS.put("\\(Map ", "(IDictionary ");
         TOKENS.put(" Map ", " IDictionary ");
         TOKENS.put(",Map ", ",IDictionary ");
         TOKENS.put("ArrayList ", "List ");
         TOKENS.put("static final", "const");
         TOKENS.put("final", "readonly");
         TOKENS.put("readonlyly", "finally");
         TOKENS.put("readonly class", "sealed class");
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
         TOKENS.put("@Retention\\(RetentionPolicy.RUNTIME\\)", "[AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]");

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
         writer.append(line.replaceAll("\\/\\*\\*", "/// <summary>"));
         writer.append("\n");
         while(lines.hasNext()) {
            String nextLine = lines.next();
            nextLine = nextLine.substring(1);
            if(nextLine.matches("^\\s*\\*\\/")) {
               if(!endSummary) {
                  writer.append(nextLine.replaceAll("\\*\\/", "/// </summary>"));
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
                  writer.append("/// </summary>");
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
   
   private static class ReplaceLicense implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         List<String> lines = stripLines(source);
         Iterator<String> iterator = lines.iterator();
         StringWriter writer = new StringWriter();
         boolean licenseDone = false;
         while(iterator.hasNext()) {
            String line = iterator.next();
            if(!licenseDone && line.matches("\\s*\\/\\*")) {
               writer.append("#region License\n");
               license(iterator, writer, line);
               writer.append("#endregion\n");
               licenseDone = true;
            } else {
               writer.append(line);
               writer.append("\n");
            }
         }
         return writer.toString();
      }
      private void license(Iterator<String> lines, StringWriter writer, String line) throws Exception {
         Pattern comment = Pattern.compile("^(\\s*)\\*.*$");
         writer.append(line.replaceAll("\\/\\*", "//"));
         writer.append("\n");
         while(lines.hasNext()) {
            String nextLine = lines.next();
            nextLine = nextLine.substring(1);
            if(nextLine.matches("^\\s*\\*\\/")) {
               writer.append(nextLine.replaceAll("\\*\\/", "//"));
               writer.append("\n");
               return;
            }
            Matcher matcher = comment.matcher(nextLine);
            if(matcher.matches()) {
               if(nextLine.matches(".*\\.java.*")) {
                  nextLine = nextLine.replaceAll("\\.java", ".cs");
               }
               writer.append(matcher.group(1));
               writer.append(nextLine.replaceAll("^\\s*\\*", "//"));
               writer.append("\n");
            }else {
               throw new IllegalStateException("Comment does not end well " + nextLine);
            }
              
         }
      }
   }
  
   private static class SubstituteAnnotations implements ConversionPhase {
      public String convert(String source, SourceDetails details) throws Exception {
         Pattern pattern = Pattern.compile("^(.+) @interface (.+)\\{.*");
         List<String> lines = stripLines(source);
         StringWriter writer = new StringWriter();
         for(String line : lines) {
            Matcher matcher = pattern.matcher(line);
            if(matcher.matches()) {
               String start = matcher.group(1);
               String type = matcher.group(2);
               writer.append(start);
               writer.append(" class ");
               writer.append(type);
               writer.append(": System.Attribute {\n");
            } else {
               writer.append(line);
               writer.append("\n");
            }
         }
         return writer.toString();
      }
   }

}
