#region Using directives
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   import org.w3c.dom.DOMConfiguration;
   import org.w3c.dom.DOMImplementation;
   import org.w3c.dom.Document;
   import org.w3c.dom.ls.DOMImplementationLS;
   import org.w3c.dom.ls.LSOutput;
   import org.w3c.dom.ls.LSSerializer;
   public class Formatter {
      private const String LS_FEATURE_KEY = "LS";
      private const String LS_FEATURE_VERSION = "3.0";
      private const String CORE_FEATURE_KEY = "Core";
      private const String CORE_FEATURE_VERSION = "2.0";
      private readonly bool preserveComments;
      public Formatter() {
         this(false);
      }
      public Formatter(bool preserveComments) {
         this.preserveComments = preserveComments;
      }
      public String Format(String source) {
         StringWriter writer = new StringWriter();
         Format(source, writer);
         return writer.toString();
      }
      public void Format(String source, Writer writer) {
         try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Reader reader = new StringReader(source);
            InputSource input = new InputSource(reader);
            Document document = builder.parse(input);
            Format(document, writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
      }
      public void Format(Document document, Writer writer) {
         DOMImplementation implementation = document.getImplementation();
         if(implementation.hasFeature(LS_FEATURE_KEY, LS_FEATURE_VERSION) && implementation.hasFeature(CORE_FEATURE_KEY, CORE_FEATURE_VERSION)) {
            DOMImplementationLS implementationLS = (DOMImplementationLS) implementation.getFeature(LS_FEATURE_KEY, LS_FEATURE_VERSION);
            LSSerializer serializer = implementationLS.createLSSerializer();
            DOMConfiguration configuration = serializer.getDomConfig();
            configuration.setParameter("format-pretty-print", Boolean.TRUE);
            configuration.setParameter("comments", preserveComments);
            LSOutput output = implementationLS.createLSOutput();
            output.setEncoding("UTF-8");
            output.setCharacterStream(writer);
            serializer.write(document, output);
         }
      }
     public String Read(File file) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        InputStream source = new FileInputStream(file);
        byte[] chunk = new byte[1024];
        int count = 0;
        while((count = source.Read(chunk)) != -1) {
           buffer.write(chunk, 0, count);
        }
        return buffer.toString("UTF-8");
     }
     /// <summary>
     /// Scripts to execute the XML formatter.
     /// #!/bin/bash
     /// xml.bat $1 $2 $3
     /// echo off
     /// java -jar c:/start/bin/xml.jar %1 %2 %3 %4
     /// </summary>
     /// <param name="list">
     /// arguments to the formatter
     /// </param>
     public void Main(String list[]) {
        List<String> values = new ArrayList<String>();
        for(String argument : list) {
           if(argument != null && argument.trim().length() > 0) {
              values.add(argument.trim());
           }
        }
        if(values.size() == 0) {
           throw new FileNotFoundException("File needs to be specified as an argument");
        }
        Formatter formatter = new Formatter();
        File file = new File(values.get(0));
        String source = formatter.Read(file); // read before opening for write
        if(values.size() == 1) {
           formatter.Format(source, new OutputStreamWriter(System.out));
        }
        else if(values.size() == 2) {
           formatter.Format(source, new OutputStreamWriter(new FileOutputStream(new File(values.get(1)))));
        }
        else {
           StringBuilder builder = new StringBuilder();
           for(String value : values) {
              builder.append("'").append(value).append("'");
           }
           throw new IllegalArgumentException("At most two arguments can be specified, you specified "+builder);
        }
     }
   }
}
