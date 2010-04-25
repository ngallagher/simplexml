#region Using directives
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class LanguageConverter : Replace {
      private const List<Class<? : ConversionPhase>> STAGE_ONE = new ArrayList<Class<? : ConversionPhase>>();
      private const List<Class<? : SubstitutionPhase>> STAGE_TWO = new ArrayList<Class<? : SubstitutionPhase>>();
      private const Map<String, String> NAMESPACE = new LinkedHashMap<String, String>();
      private const Map<String, String> USING = new LinkedHashMap<String, String>();
      private const Map<String, String> FILES = new LinkedHashMap<String, String>();
      private const String INDENT = "   ";
      static {
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml");
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml\\core", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Core");
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml\\filter", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Filter");
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml\\strategy", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Strategy");
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml\\stream", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Stream");
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml\\convert", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Convert");
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml\\transform", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Transform");
         FILES.put("C:\\Users\\niall\\Workspace\\xml\\src\\test\\java\\org\\simpleframework\\xml\\util", "C:\\Users\\niall\\Workspace\\SimpleFramework\\SimpleFramework\\SimpleFramework\\src\\main\\Xml\\Util");
      }
      static {
         USING.put("import SimpleFramework.Xml.Util.*","using SimpleFramework.Xml.Util;");
         USING.put("import SimpleFramework.Xml.Filter.*", "using SimpleFramework.Xml.Filter;");
         USING.put("import SimpleFramework.Xml.Strategy.*", "using SimpleFramework.Xml.Strategy;");
         USING.put("import SimpleFramework.Xml.Core.*","using SimpleFramework.Xml.Core;");
         USING.put("import SimpleFramework.Xml.Util.*", "using SimpleFramework.Xml.Util;");
         USING.put("import SimpleFramework.Xml.Stream.*","using SimpleFramework.Xml.Stream;");
         USING.put("import SimpleFramework.Xml.*","using SimpleFramework.Xml;");
         USING.put("import java.util.*","using System.Collections.Generic;");
      }
      static {
         NAMESPACE.put("package SimpleFramework.Xml;","namespace SimpleFramework.Xml {");
         NAMESPACE.put("package SimpleFramework.Xml.Util;","namespace SimpleFramework.Xml.Util {");
         NAMESPACE.put("package SimpleFramework.Xml.Filter;", "namespace SimpleFramework.Xml.Filter {");
         NAMESPACE.put("package SimpleFramework.Xml.Strategy;", "namespace SimpleFramework.Xml.Strategy {");
         NAMESPACE.put("package SimpleFramework.Xml.Core;","namespace SimpleFramework.Xml.Core {");
         NAMESPACE.put("package SimpleFramework.Xml.Util;", "namespace SimpleFramework.Xml.Util {");
         NAMESPACE.put("package SimpleFramework.Xml.Stream;","namespace SimpleFramework.Xml.Stream {");
      }
      static {
         STAGE_ONE.Add(CanonicalizeFile.class);
         STAGE_ONE.Add(DefineType.class);
         STAGE_ONE.Add(PopulateUsing.class);
         STAGE_ONE.Add(AddUsing.class);
         STAGE_ONE.Add(StripImports.class);
         STAGE_ONE.Add(CreateNamespace.class);
         STAGE_ONE.Add(GetFields.class);
         STAGE_ONE.Add(ReplaceComments.class);
         STAGE_ONE.Add(ReplaceDocumentation.class);
         STAGE_ONE.Add(ReplaceKeyWords.class);
         STAGE_ONE.Add(ReplaceMethodConventions.class);
         STAGE_ONE.Add(StripCrap.class);
         STAGE_ONE.Add(ReplaceLicense.class);
         STAGE_ONE.Add(SubstituteAnnotations.class);
         STAGE_ONE.Add(ConvertAnnotationAttributes.class);
         STAGE_ONE.Add(SetAnnotationAttributes.class);
         STAGE_ONE.Add(ConvertClassBeanMethods.class);
      }
      static {
         STAGE_TWO.Add(SubstituteMethods.class);
      }
      public void Main(String list[]) {
         SourceProject project = new SourceProject();
         for(String from : FILES.keySet()) {
            List<File> files = getFiles(new File(from));
            String to = FILES.get(from);
           // files = Collections.singletonList(new File("C:\\Users\\niall\\Workspace\\xml\\src\\main\\java\\org\\simpleframework\\xml\\core\\ParameterContact.java"));
            for(File file : files) {
               SourceDetails details = new SourceDetails(file, new File(to, file.Name.replaceAll("\\.java", ".cs")));
               String text = getFile(file);
               details.Text = text;
               for(Class<? : ConversionPhase> phaseType : STAGE_ONE) {
                  Constructor<? : ConversionPhase> factory = phaseType.getDeclaredConstructor();
                  if(!factory.isAccessible()) {
                     factory.setAccessible(true);
                  }
                  ConversionPhase phase = factory.newInstance();
                  details.setText(phase.Convert(details.Text, details));
               }
               project.AddSource(details);
            }
         }
         for(SourceDetails details : project.GetDetails()) {
            for(Class<? : SubstitutionPhase> phaseType : STAGE_TWO) {
               Constructor<? : SubstitutionPhase> factory = phaseType.getDeclaredConstructor();
               if(!factory.isAccessible()) {
                  factory.setAccessible(true);
               }
               SubstitutionPhase phase = factory.newInstance();
               details.setText(phase.Convert(details.Text, details, project));
            }
         }
         List<String> newFiles = new ArrayList<String>();
         for(SourceDetails details : project.GetDetails()) {
            File saveAs = details.Destination;
            save(saveAs, details.Text);
            newFiles.Add(saveAs.getCanonicalPath().replaceAll("^.*src", "src"));
         }
         for(String entry : newFiles) {
            System.out.println(" <Compile Include=\""+entry+"\"/>");
         }
      }
      public String ConvertMethod(String originalMethod, MethodType type) {
         if(type == null || type == MethodType.NORMAL) {
            if(originalMethod != null && !Character.isUpperCase(originalMethod.charAt(0))){
               StringBuilder builder = new StringBuilder(originalMethod.length());
               char first = originalMethod.charAt(0);
               builder.append(Character.toUpperCase(first));
               builder.append(originalMethod.substring(1));
               return builder.ToString();
            }
         } else if(type == MethodType.GET) {
            if(originalMethod != null){
               return originalMethod.substring(3);
            }
         } else if(type == MethodType.SET) {
            if(originalMethod != null){
               return originalMethod.substring(3);
            }
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
         private List<MethodSignature> methods = new ArrayList<MethodSignature>();
         private String packageName;
         private SourceType type;
         private String name;
         private File destination;
         private File source;
         private String text;
         public SourceDetails(File source, File destination) {
            this.using.Add("using System;");
            this.destination = destination;
            this.source = source;
         }
         public String Text {
            get {
               return text;
            }
         }
         //public String GetText() {
         //   return text;
         //}
            this.text = text;
         }
         public File Destination {
            get {
               return destination;
            }
         }
         //public File GetDestination() {
         //   return destination;
         //}
            return source;
         }
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
            this.name = name;
         }
         public String Package {
            get {
               return packageName;
            }
         }
         //public String GetPackage() {
         //   return packageName;
         //}
            this.packageName = packageName;
         }
         public String FullyQualifiedName {
            get {
               return String.format("%s.%s", packageName, name);
            }
         }
         //public String GetFullyQualifiedName() {
         //   return String.format("%s.%s", packageName, name);
         //}
            return type;
         }
         public SourceType Type {
            set {
               this.type = value;
            }
         }
         //public void SetType(SourceType type) {
         //   this.type = type;
         //}
            return using;
         }
         public void AddMethod(MethodSignature method) {
            methods.Add(method);
         }
         public List<MethodSignature> Methods {
            get {
               return methods;
            }
         }
         //public List<MethodSignature> GetMethods() {
         //   return methods;
         //}
            imports.Add(importClass);
         }
         public List<String> Imports {
            get {
               return imports;
            }
         }
         //public List<String> GetImports() {
         //   return imports;
         //}
            return attributes;
         }
         public void AddAttribute(String type, String attribute) {
            attributes.put(attribute, type);
         }
         public void AddUsing(String usingValue) {
            using.Add(usingValue);
         }
         public Map<String, String> getFields() {
            return fields;
         }
         public void AddField(String name, String type) {
            fields.put(name, type);
         }
      }
      private static enum MethodType {
         GET,
         SET,
         NORMAL
      }
      private static class MethodSignature {
         private readonly String name;
         private readonly String value;
         private readonly MethodType type;
         public MethodSignature(String name, MethodType type, String value) {
            this.name = name;
            this.type = type;
            this.value = value;
         }
         public String ToString() {
            return String.format("[%s][%s][%s]", name, type, value);
         }
      }
      private static class SourceProject {
         private Map<String, List<SourceDetails>> packages = new HashMap<String, List<SourceDetails>>();
         private Map<String, SourceDetails> names = new HashMap<String, SourceDetails>();
         private List<SourceDetails> all = new ArrayList<SourceDetails>();
         public List<SourceDetails> Details {
            get {
               return all;
            }
         }
         //public List<SourceDetails> GetDetails() {
         //   return all;
         //}
            String packageName = details.Package;
            String name = details.Name;
            List<SourceDetails> packageFiles = packages.get(packageName);
            if(packageFiles == null) {
               packageFiles = new ArrayList<SourceDetails>();
               packages.put(packageName, packageFiles);
            }
            packageFiles.Add(details);
            all.Add(details);
            names.put(name, details);
         }
         public SourceDetails GetDetails(String name) {
            return names.get(name);
         }
      }
      private static abstract class SubstitutionPhase {
         public Map<String, String> calculateSubstututions(SourceDetails details, SourceProject project) {
            Map<String, String> substitutes = new HashMap<String, String>();
            for(String field : details.getFields().keySet()) {
               String type = details.getFields().get(field);
               SourceDetails fieldDetails = project.GetDetails(type);
               if(fieldDetails != null) {
                  PopulateFrom(fieldDetails, field, substitutes);
               }
            }
            PopulateFrom(details, null, substitutes);
            return substitutes;
         }
         public void PopulateFrom(SourceDetails details, String field, Map<String, String> substitutes) {
            List<MethodSignature> methods = details.Methods;
            for(MethodSignature originalMethod : details.Methods) {
               if(originalMethod.type == MethodType.GET) {
                  substitutes.put(originalMethod.name+"\\(\\)", ConvertMethod(originalMethod.name, originalMethod.type));
               } else if(originalMethod.type == MethodType.SET) {
                  substitutes.put(originalMethod.name+"\\([a-zA-Z\\s]+\\)", ConvertMethod(originalMethod.name, originalMethod.type) + " = "+originalMethod.value);
               } else {
                  substitutes.put(originalMethod.name+"\\(", ConvertMethod(originalMethod.name, MethodType.NORMAL)+"\\(");
               }
            }
            if(field != null && !field.equals("")) {
               for(MethodSignature originalMethod : methods) {
                  String originalToken = String.format("%s.%s", field, originalMethod.name);
                  if(originalMethod.type == MethodType.GET) {
                     String token = String.format("%s.%s", field, ConvertMethod(originalMethod.name, originalMethod.type));
                     substitutes.put(originalToken+"\\(\\)", token);
                  } else if(originalMethod.type == MethodType.SET) {
                     String token = String.format("%s.%s = %s", field, ConvertMethod(originalMethod.name, originalMethod.type), originalMethod.value);
                     substitutes.put(originalToken+"\\([a-zA-Z\\s]+\\)", token);
                  } else {
                     String token = String.format("%s.%s\\(", field, ConvertMethod(originalMethod.name, MethodType.NORMAL));
                     substitutes.put(originalToken+"\\(", token);
                  }
               }
            }
         }
         public abstract String Convert(String source, SourceDetails details, SourceProject project);
      }
      private static interface ConversionPhase {
         public String Convert(String source, SourceDetails details);
      }
      public static class SubstituteMethods : SubstitutionPhase {
         public String Convert(String source, SourceDetails details, SourceProject project) {
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
            return writer.ToString();
         }
      }
      public static class CanonicalizeFile : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
      //      if(source.indexOf('\t') != -1) {
        //       throw new Exception("File contains tab "+details.Source);
          //  }
            return source.replaceAll("\r\n", "\n").replaceAll("\r", "\n");
         }
      }
      public static class GetFields : ConversionPhase {
         private const List<String> MODIFIERS = new ArrayList<String>();
         static {
            MODIFIERS.Add("private readonly");
            MODIFIERS.Add("private");
            MODIFIERS.Add("protected");
            MODIFIERS.Add("protected readonly");
         }
         public String Convert(String source, SourceDetails details) {
            List<Pattern> patterns = new ArrayList<Pattern>();
            for(String modifier : MODIFIERS) {
               patterns.Add(Pattern.compile("^\\s+"+modifier+"\\s+([a-zA-Z]+)\\s+([a-zA-Z]+)\\s*;.*$"));
            }
            List<String> lines = stripLines(source);
            for(String line : lines) {
               for(Pattern pattern : patterns) {
                  Matcher matcher = pattern.matcher(line);
                  if(matcher.matches()) {
                     String type = matcher.group(1);
                     String name = matcher.group(2);
                     details.AddField(name, type);
                     break;
                  }
               }
            }
            return source;
         }
      }
      public static class ConvertAnnotationAttributes : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
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
                  details.AddAttribute(type, attribute);
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
            return writer.ToString();
         }
      }
      private static class ConvertClassBeanMethods : ConversionPhase {
         private const Pattern RIGHT_BRACE = Pattern.compile("^.*\\{.*$");
         private const Pattern LEFT_BRACE = Pattern.compile("^.*\\}.*$");
         private const List<String> TYPE_MODIFIERS = new ArrayList<String>();
         private const List<String> METHOD_MODIFIERS = new ArrayList<String>();
         static {
            TYPE_MODIFIERS.Add("class");
            TYPE_MODIFIERS.Add("public class");
            TYPE_MODIFIERS.Add("abstract class");
            TYPE_MODIFIERS.Add("static class");
            TYPE_MODIFIERS.Add("sealed class");
            TYPE_MODIFIERS.Add("private static abstract class");
            TYPE_MODIFIERS.Add("private sealed class");
            TYPE_MODIFIERS.Add("private class");
            TYPE_MODIFIERS.Add("private static sealed class");
            TYPE_MODIFIERS.Add("private static class");
            TYPE_MODIFIERS.Add("public sealed class");
            TYPE_MODIFIERS.Add("public static sealed class");
            TYPE_MODIFIERS.Add("public static class");
            TYPE_MODIFIERS.Add("protected sealed class");
            TYPE_MODIFIERS.Add("protected class");
            TYPE_MODIFIERS.Add("protected static sealed class");
            TYPE_MODIFIERS.Add("protected static class");
            TYPE_MODIFIERS.Add("static sealed class");
         }
         static {
            METHOD_MODIFIERS.Add("public abstract");
            METHOD_MODIFIERS.Add("public");
            METHOD_MODIFIERS.Add("protected abstract");
            METHOD_MODIFIERS.Add("protected");
            METHOD_MODIFIERS.Add("private");
         }
         public Matcher Match(String line, MethodType type) {
            for(String modifier : METHOD_MODIFIERS) {
               if(type == MethodType.GET) {
                  Pattern pattern = Pattern.compile("^(\\s+)"+modifier+"\\s+(.*)\\s+Get([a-zA-Z]+)\\(\\)\\s*.*$");
                  Matcher matcher = pattern.matcher(line);
                  if(matcher.matches()) {
                     return matcher;
                  }
               } else if(type == MethodType.SET) {
                  Pattern pattern = Pattern.compile("^(\\s+)"+modifier+"\\s+void\\s+Set([a-zA-Z]+)\\((.*)\\s+([a-zA-Z]+)\\)\\s*.*$");
                  Matcher matcher = pattern.matcher(line);
                  if(matcher.matches()) {
                     return matcher;
                  }
               }
            }
            return null;
         }
         public String Convert(String source, SourceDetails details) {
            if(details.Type == SourceType.CLASS || details.Type == SourceType.INTERFACE) {
               List<String> lines = stripLines(source);
               PropertyMap properties = new PropertyMap(details);
               StringWriter writer = new StringWriter();
               String qualifier = "";
               Extract(source, details, properties);
               for(int i = 0; i < lines.size(); i++) {
                  String line = lines.get(i);
                  qualifier += Qualifier(line);
                  Matcher getter = Match(line, MethodType.GET);
                  if(getter != null) {
                     String name = getter.group(3);
                     String fullName = qualifier+"."+name;
                     Property property = properties.GetProperty(fullName);
                     if(!property.IsGettable()) {
                        throw new IllegalStateException("The line '"+line+"' was not extracted from "+details.FullyQualifiedName);
                     }
                     if(!property.IsDone()) {
                        Write(details, property..indent, property, writer);
                     }
                     String indent = property..indent;
                     int indentLength = indent.length();
                     for(int j = 0; j <= property..lineCount; j++) {
                        line = lines.get(i++);
                        writer.append(indent);
                        writer.append("//");
                        if(line.length() < indentLength) {
                           throw new IllegalStateException("Line '"+line+"' is out of place in " + writer.ToString());
                        }
                        writer.append(line.substring(indentLength));
                        writer.append("\n");
                     }
                  } else {
                     Matcher setter = Match(line, MethodType.SET);
                     if(setter != null) {
                        String name = setter.group(2);
                        String fullName = qualifier+"."+name;
                        Property property = properties.GetProperty(fullName);
                        if(!property.IsSettable()) {
                           throw new IllegalStateException("The line '"+line+"' was not extracted from "+details.FullyQualifiedName);
                        }
                        if(!property.IsDone()) {
                           Write(details, property.Set().indent, property, writer);
                        }
                        if(property.Set() == null) {
                           throw new IllegalStateException("Can not find setter '"+fullName+"' from line '"+line+"' from "+writer.ToString());
                        }
                        String indent = property.Set().indent;
                        int indentLength = indent.length();
                        for(int j = 0; j <= property.Set().lineCount; j++) {
                           line = lines.get(i++);
                           writer.append(indent);
                           writer.append("//");
                           if(line.length() < indentLength) {
                              throw new IllegalStateException("Line '"+line+"' is out of place in " + writer.ToString());
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
               return writer.ToString();
            }
            return source;
         }
         public void Write(SourceDetails details, String indent, Property property, StringWriter writer) {
            if(property.Verify()) {
               if(property.get != null) {
                  writer.append(property.get.indent);
                  writer.append("public ");
                  if(property.get.isAbstract && details.Type == SourceType.CLASS) {
                     writer.append("abstract ");
                  }
                  writer.append(property.get.type);
                  writer.append(" ");
                  writer.append(property.get.name);
                  writer.append(" {\n");
                  writer.append(property.get.indent);
                  if(property.get.isAbstract) {
                     writer.append("   get;\n");
                  } else {
                     writer.append("   get {\n");
                     List<String> lines = stripLines(property.get.content);
                     for(String line : lines) {
                        writer.append("   ");
                        writer.append(line);
                        writer.append("\n");
                     }
                  }
               }
               if(property.set != null) {
                  if(property.get == null) {
                     writer.append(property.set.indent);
                     writer.append("public ");
                     if(property.set.isAbstract && details.Type == SourceType.CLASS) {
                        writer.append("abstract ");
                     }
                     writer.append(property.set.type);
                     writer.append(" ");
                     writer.append(property.set.name);
                     writer.append(" {\n");
                  }
                  writer.append(property.set.indent);
                  if(property.set.isAbstract) {
                     writer.append("   set;\n");
                  } else {
                     writer.append("   set {\n");
                     List<String> lines = stripLines(property.set.content);
                     for(String line : lines) {
                        writer.append("   ");
                        writer.append(line);
                        writer.append("\n");
                     }
                  }
               }
               writer.append(indent);
               writer.append("}");
               writer.append("\n");
            }
            property.Done();
         }
         public void Extract(String source, SourceDetails details, PropertyMap properties) {
            List<String> lines = stripLines(source);
            String qualifier = "";
            for(int i = 0; i < lines.size(); i++) {
               String line = lines.get(i);
               qualifier += Qualifier(line);
               Matcher getter = Match(line, MethodType.GET);
               if(getter != null) {
                  String indent = getter.group(1);
                  String type = getter.group(2);
                  String name = getter.group(3);
                  StringBuilder writer = new StringBuilder();
                  bool isAbstract = false;
                  int lineCount = 0;
                  int braces = 0;
                  i++;
                  if(!line.matches(".*;\\s*$") && !line.matches(".*abstract.*")) { // abstract ends in ;
                     for(; braces >= 0 && i < lines.size(); i++) {
                        line = lines.get(i);
                        Matcher right = RIGHT_BRACE.matcher(line);
                        if(right.matches()) {
                           braces++;
                        }
                        Matcher left = LEFT_BRACE.matcher(line);
                        if(left.matches()) {
                           braces--;
                        }
                        writer.append(line);
                        writer.append("\n");
                        lineCount++;
                     }
                  } else {
                     isAbstract = true;
                  }
                  String fullName = qualifier+"."+name;
                  if(properties.GetProperty(fullName) != null && properties.GetProperty(fullName).IsGettable()) {
                     throw new IllegalStateException("The property '"+fullName+"' is defined twice in "+details.FullyQualifiedName+" on line '"+line+"' in "+writer.ToString());
                  }
                  properties.AddGetProperty(fullName, new MethodDetail(name, type, indent, writer.ToString(), lineCount, isAbstract));
               } else {
                  Matcher setter = Match(line, MethodType.SET);
                  if(setter != null) {
                     String indent = setter.group(1);
                     String name = setter.group(2);
                     String type = setter.group(3);
                     String value = setter.group(4);
                     StringBuilder writer = new StringBuilder();
                     bool isAbstract = false;
                     int lineCount = 0;
                     int braces = 0;
                     i++;
                     if(!line.matches(".*;\\s*$") && !line.matches(".*abstract.*")) { // abstract ends in ;
                        for(; braces >= 0 && i < lines.size(); i++) {
                           line = lines.get(i);
                           Matcher right = RIGHT_BRACE.matcher(line);
                           if(right.matches()) {
                              braces++;
                           }
                           Matcher left = LEFT_BRACE.matcher(line);
                           if(left.matches()) {
                              braces--;
                           }
                           writer.append(line);
                           writer.append("\n");
                           lineCount++;
                        }
                     } else {
                        isAbstract = true;
                     }
                     String fullName = qualifier+"."+name;
                     if(properties.GetProperty(fullName) != null && properties.GetProperty(fullName).IsSettable()) {
                        throw new IllegalStateException("The property '"+fullName+"' is defined twice in "+details.FullyQualifiedName);
                     }
                     String content = writer.ToString();
                     content = content.replaceAll(" value\\)", " _value)");
                     content = content.replaceAll(" value;", " _value;");
                     content = content.replaceAll(" value=", " _value=\\");
                     content = content.replaceAll(" value\\.", " _value.");
                     content = content.replaceAll("\\(value\\)", "(_value)");
                     content = content.replaceAll("\\(value\\.", "(_value.");
                     content = content.replaceAll("=value;", "=_value;");
                     content = content.replaceAll("=value\\.", "=_value.");
                     content = content.replaceAll(" "+value+"\\)", " value)");
                     content = content.replaceAll(" "+value+";", " value;");
                     content = content.replaceAll(" "+value+"=", " value=\\");
                     content = content.replaceAll(" "+value+"\\.", " value.");
                     content = content.replaceAll("\\("+value+"\\)", "(value)");
                     content = content.replaceAll("\\("+value+"\\.", "(value.");
                     content = content.replaceAll("="+value+";", "=value;");
                     content = content.replaceAll("="+value+"\\.", "=value.");
                     properties.AddSetProperty(fullName, new MethodDetail(name, type, indent, content, lineCount, isAbstract));
                  }
               }
            }
         }
         public String Qualifier(String line) {
            for(String modifier : TYPE_MODIFIERS) {
               Pattern pattern = Pattern.compile("^\\s*"+modifier+"\\s+([a-zA-Z]+).*$");
               Matcher matcher = pattern.matcher(line);
               if(matcher.matches()) {
                  return "."+matcher.group(1);
               }
            }
            return "";
         }
         private class MethodDetail {
            public readonly String type;
            public readonly String name;
            public readonly String indent;
            public readonly String content;
            public readonly int lineCount;
            public readonly bool isAbstract;
            public MethodDetail(String name, String type, String indent, String content, int lineCount, bool isAbstract) {
               this.name = name;
               this.type = type;
               this.indent = indent;
               this.content = content;
               this.lineCount = lineCount;
               this.isAbstract = isAbstract;
            }
            public String ToString() {
               return String.format("[%s][%s][%s]", name, type, isAbstract);
            }
         }
         private class PropertyMap {
            private Map<String, Property> properties = new HashMap<String, Property>();
            private SourceDetails details;
            public PropertyMap(SourceDetails details) {
               this.details = details;
            }
            public Property GetProperty(String name) {
               return properties.get(name);
            }
            public void AddSetProperty(String name, MethodDetail detail) {
               Property property = properties.get(name);
               if(property == null) {
                  property = new Property();
                  properties.put(name, property);
               }
               if(property.Set() != null) {
                  throw new IllegalStateException("The property '"+name+"' is defined twice in "+details.FullyQualifiedName);
               }
               if(detail == null) {
                  throw new IllegalStateException("Can not set a null property");
               }
               property.AddSetMethod(detail);
            }
            public void AddGetProperty(String name, MethodDetail detail) {
               Property property = properties.get(name);
               if(property == null) {
                  property = new Property();
                  properties.put(name, property);
               }
               if(property. != null) {
                  throw new IllegalStateException("The property '"+name+"' is defined twice in "+details.FullyQualifiedName);
               }
               if(detail == null) {
                  throw new IllegalStateException("Can not set a null property");
               }
               property.AddGetMethod(detail);
            }
         }
         private class Property {
            private MethodDetail get;
            private MethodDetail set;
            private bool done;
            public MethodDetail Get() {
               return get;
            }
            public MethodDetail Set() {
               return set;
            }
            public bool IsGettable() {
               return get != null;
            }
            public bool IsSettable() {
               return set != null;
            }
            public void AddSetMethod(MethodDetail detail) {
               this.set = detail;
            }
            public void AddGetMethod(MethodDetail detail) {
               this.get = detail;
            }
            public bool IsDone() {
               return done;
            }
            public void Done() {
               done = true;
            }
            public bool Verify() {
               if(get != null && set != null) {
                  if(!get.name.equals(set.name)) {
                     throw new IllegalStateException("Property names do not match for '"+get.name+"' and '"+set.name+"'");
                  }
                  if(!get.type.equals(set.type)) {
                     throw new IllegalStateException("Property types do not match for '"+get.type+"' and '"+set.type+"'");
                  }
               }
               return true;
            }
            public String ToString() {
               return String.format("GET -> %s SET -> %s", get, set);
            }
         }
      }
      public static class SetAnnotationAttributes : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
            if(details.Type == SourceType.ANNOTATION) {
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
               return writer.ToString();
            }
            return source;
         }
      }
      private static class DefineType : ConversionPhase {
         private const Map<Pattern, SourceType> PATTERNS = new HashMap<Pattern, SourceType>();
         static {
            PATTERNS.put(Pattern.compile("^public\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
            PATTERNS.put(Pattern.compile("^public\\s+readonly\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
            PATTERNS.put(Pattern.compile("^class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
            PATTERNS.put(Pattern.compile("^public\\s+abstract\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
            PATTERNS.put(Pattern.compile("^abstract\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
            PATTERNS.put(Pattern.compile("^readonly\\s+class\\s+([a-zA-Z]*).*"), SourceType.CLASS);
            PATTERNS.put(Pattern.compile("^public\\s+@interface\\s+([a-zA-Z]*).*"), SourceType.ANNOTATION);
            PATTERNS.put(Pattern.compile("^@interface\\s+([a-zA-Z]*).*"), SourceType.ANNOTATION);
            PATTERNS.put(Pattern.compile("^public\\s+interface\\s+([a-zA-Z]*).*"), SourceType.INTERFACE);
            PATTERNS.put(Pattern.compile("^interface\\s+([a-zA-Z]*).*"), SourceType.INTERFACE);
            PATTERNS.put(Pattern.compile("^public\\s+enum\\s+([a-zA-Z]*).*"), SourceType.ENUM);
            PATTERNS.put(Pattern.compile("^enum\\s+([a-zA-Z]*).*"), SourceType.ENUM);
         }
         public String Convert(String source, SourceDetails details) {
            List<String> lines = stripLines(source);
            for(String line : lines) {
               for(Pattern pattern : PATTERNS.keySet()) {
                  Matcher matcher = pattern.matcher(line);
                  if(matcher.matches()) {
                     SourceType type = PATTERNS.get(pattern);
                     String name = matcher.group(1);
                     details.Type = type;
                     details.Name = name;
                     return source;
                  }
               }
            }
            throw new IllegalStateException("File can not be classified " + details.Source);
         }
      }
      private static class PopulateUsing : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
            List<String> lines = stripLines(source);
            for(String line : lines) {
               if(line.matches("^import.*")) {
                  line = line.trim();
                  for(String importName : USING.keySet()) {
                     if(line.matches(importName)) {
                        importName = USING.get(importName);
                        details.AddUsing(importName);
                        break;
                     }
                  }
               }
            }
            return source;
         }
      }
      private static class AddUsing : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
            Pattern pattern = Pattern.compile("^package\\s+([a-zA-Z\\.]*)\\s*;.*");
            List<String> lines = stripLines(source);
            StringWriter writer = new StringWriter();
            bool importsDone = false;
            for(String line : lines) {
               if(!importsDone) {
                  Matcher matcher = pattern.matcher(line);
                  if(matcher.matches()) {
                     String packageName = matcher.group(1);
                     writer.append("\n#region Using directives\n");
                     for(String using : details.Using) {
                        writer.append(using);
                        writer.append("\n");
                     }
                     writer.append("\n#endregion\n");
                     details.Package = packageName;
                     importsDone = true;
                  }
               }
               writer.append(line);
               writer.append("\n");
            }
            return writer.ToString();
         }
      }
      private static class StripImports : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
            Pattern pattern = Pattern.compile("^import\\s+([a-zA-Z\\.]*)\\s*;.*$");
            List<String> lines = stripLines(source);
            StringWriter writer = new StringWriter();
            for(String line : lines) {
               Matcher matcher = pattern.matcher(line);
               if(matcher.matches()) {
                  String importClass = matcher.group(1);
                  details.AddImport(importClass);
               } else {
                  writer.append(line);
                  writer.append("\n");
               }
            }
            return writer.ToString();
         }
      }
      private static class CreateNamespace : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
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
            return writer.ToString();
         }
      }
      private static class ReplaceMethodConventions : ConversionPhase {
         private const List<String> MODIFIERS = new ArrayList<String>();
         static {
            MODIFIERS.Add("public static");
            MODIFIERS.Add("private static");
            MODIFIERS.Add("protected static");
            MODIFIERS.Add("public");
            MODIFIERS.Add("private");
            MODIFIERS.Add("protected");;
         }
         public String Convert(String source, SourceDetails details) {
            List<String> lines = stripLines(source);
            StringWriter writer = new StringWriter();
            main: for(String line : lines) {
               for(String modifier : MODIFIERS) {
                  Pattern methodMatch = Pattern.compile("^(\\s*)"+modifier+"\\s+([a-zA-Z\\[\\]\\<\\>\\s]+)\\s+([a-zA-Z]+)\\((.*)\\).*$");
                  Matcher matcher = methodMatch.matcher(line);
                  if(matcher.matches()) {
                     String indent = matcher.group(1);
                     String type = matcher.group(2);
                     String originalMethod = matcher.group(3);;
                     String signature = matcher.group(4);
                     String method = ConvertMethod(originalMethod, MethodType.NORMAL);
                     writer.append(indent);
                     writer.append("public ");
                     writer.append(type);
                     writer.append(" ");
                     writer.append(method);
                     writer.append("(");
                     writer.append(signature);
                     if(line.matches(".*;\\s*") || details.Type == SourceType.INTERFACE) {
                        writer.append(");\n");
                     } else {
                        writer.append(") {\n");
                     }
                     Add(details, originalMethod, signature, type);
                     continue main;
                  }
               }
               writer.append(line);
               writer.append("\n");
            }
            return writer.ToString();
         }
         public void Add(SourceDetails details, String originalMethod, String signature, String type) {
            if(originalMethod.startsWith("get")) {
               if(signature == null || signature.equals("")) {
                  details.AddMethod(new MethodSignature(originalMethod, MethodType.GET, null));
               } else {
                  details.AddMethod(new MethodSignature(originalMethod, MethodType.NORMAL, null));
               }
            } else if(originalMethod.startsWith("set")) {
               if(signature != null && signature.indexOf(" ") != -1 && signature.indexOf(",") == -1 && type.equals("void")) {
                  details.AddMethod(new MethodSignature(originalMethod, MethodType.SET, signature.split("\\s+")[1]));
               }else {
                  details.AddMethod(new MethodSignature(originalMethod, MethodType.NORMAL, null));
               }
            } else {
               details.AddMethod(new MethodSignature(originalMethod, MethodType.NORMAL, null));
            }
         }
      }
      private static class StripCrap : ConversionPhase {
         private const List<String> TOKENS = new ArrayList<String>();
         static {
            TOKENS.Add("^\\s*\\/\\/\\/\\s*$");
            TOKENS.Add("^\\s*$");
         }
         public String Convert(String source, SourceDetails details) {
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
            return writer.ToString();
         }
      }
      private static class ReplaceKeyWords : ConversionPhase {
         private const Map<String, String> TOKENS = new LinkedHashMap<String, String>();
         static {
            TOKENS.put("<c>", "<c>");
            TOKENS.put("</c>", "</c>");
            TOKENS.put("</code>", "</c>");
            TOKENS.put("</code>", "</c>");
            TOKENS.put("\\(List ", "(List ");
            TOKENS.put(" List ", " List ");
            TOKENS.put(",List ", ",List ");
            TOKENS.put("Dictionary ", "Dictionary ");
            TOKENS.put("\\(Dictionary ", "(Dictionary ");
            TOKENS.put(" Dictionary ", " Dictionary ");
            TOKENS.put(",Dictionary ", ",Dictionary ");
            TOKENS.put("List ", "List ");
            TOKENS.put("const", "const");
            TOKENS.put("readonly", "readonly");
            TOKENS.put("finally", "finally");
            TOKENS.put("sealed class", "sealed class");
            TOKENS.put("sealed class", "sealed class");
            TOKENS.put("bool", "bool");
            TOKENS.put(":", ":");
            TOKENS.put(":", ":");
            TOKENS.put("\\)\\s*throws\\s.*\\{", ") {");
            TOKENS.put("\\)\\s*throws\\s.*;", ");");
            TOKENS.put("SimpleFramework.Xml.Util","SimpleFramework.Xml.Util");
            TOKENS.put("SimpleFramework.Xml.Filter", "SimpleFramework.Xml.Filter");
            TOKENS.put("SimpleFramework.Xml.Strategy", "SimpleFramework.Xml.Strategy");
            TOKENS.put("SimpleFramework.Xml.Core","SimpleFramework.Xml.Core");
            TOKENS.put("SimpleFramework.Xml.Util", "SimpleFramework.Xml.Util");
            TOKENS.put("SimpleFramework.Xml.Stream","SimpleFramework.Xml.Stream");
            TOKENS.put("SimpleFramework.Xml","SimpleFramework.Xml");
            TOKENS.put("@Retention\\(RetentionPolicy.RUNTIME\\)", "[AttributeUsage(AttributeTargets.Class | AttributeTargets.Field | AttributeTargets.Method)]");
         }
         public String Convert(String source, SourceDetails details) {
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
            return writer.ToString();
         }
      }
      private static class ReplaceDocumentation : ConversionPhase {
         private const Map<String, String> TOKENS = new HashMap<String, String>();
         static {
            TOKENS.put("return", "returns");
            TOKENS.put("see", "seealso");
         }
         public String Convert(String source, SourceDetails details) {
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
            return writer.ToString();
         }
      }
      private static class ReplaceComments : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
            List<String> lines = stripLines(source);
            Iterator<String> iterator = lines.iterator();
            StringWriter writer = new StringWriter();
            while(iterator.hasNext()) {
               String line = iterator.next();
               if(line.matches("\\s*\\/\\*\\*")) {
                  Comment(iterator, writer, line);
               } else {
                  writer.append(line);
                  writer.append("\n");
               }
            }
            return writer.ToString();
         }
         public void Comment(Iterator<String> lines, StringWriter writer, String line) {
            Pattern normalComment = Pattern.compile("^(\\s*)\\*.*$");
            Pattern parameterComment = Pattern.compile("^(\\s*)\\*.*@.*$");
            bool endSummary = false;
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
      private static class ReplaceLicense : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
            List<String> lines = stripLines(source);
            Iterator<String> iterator = lines.iterator();
            StringWriter writer = new StringWriter();
            bool licenseDone = false;
            while(iterator.hasNext()) {
               String line = iterator.next();
               if(!licenseDone && line.matches("\\s*\\/\\*")) {
                  writer.append("#region License\n");
                  License(details, iterator, writer, line);
                  writer.append("#endregion\n");
                  licenseDone = true;
               } else {
                  if(!line.matches("^\\s*$")) {
                     licenseDone = true; // no license found
                  }
                  writer.append(line);
                  writer.append("\n");
               }
            }
            return writer.ToString();
         }
         public void License(SourceDetails details, Iterator<String> lines, StringWriter writer, String line) {
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
                  throw new IllegalStateException("Comment does not end well '" + nextLine+"' in file "+details.Source.getCanonicalPath());
               }
            }
         }
      }
      private static class SubstituteAnnotations : ConversionPhase {
         public String Convert(String source, SourceDetails details) {
            Pattern pattern = Pattern.compile("^(.+) class (.+)\\: System.Attribute {
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
            return writer.ToString();
         }
      }
   }
}
