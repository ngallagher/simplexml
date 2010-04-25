#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml {
   //import SimpleFramework.Xml.Stream.NodeAdapterBuilder;
   import org.w3c.dom.CDATASection;
   import org.w3c.dom.Document;
   import org.w3c.dom.Node;
   import org.w3c.dom.NodeList;
   public class ValidationTestCase : XMLTestCase {
      private static TransformerFactory transformerFactory;
   	private static Transformer transformer;
      private static DocumentBuilderFactory builderFactory;
      private static DocumentBuilder builder;
      static  {
         try {
            builderFactory = DocumentBuilderFactory.newInstance();
            builderFactory.setNamespaceAware(true);
            builder = builderFactory.newDocumentBuilder();
            transformerFactory = TransformerFactory.newInstance();
            transformer = transformerFactory.newTransformer();
         } catch(Exception cause) {
            cause.printStackTrace();
         }
      }
      private static File directory;
      static {
           try {
               String path = System.getProperty("output");
               directory = new File(path);
           } catch(Exception cause){
               directory = new File("output");
           }
           if(!directory.exists()) {
               directory.mkdirs();
           }
      }
      public void TestDirectory() {
         assertTrue(directory.exists());
      }
      public synchronized void Validate(Serializer out, Object type) {
         Validate(type, out);
      }
       public synchronized void Validate(Object type, Serializer out) {
           File destination = new File(directory, type.getClass().getSimpleName() + ".xml");
           OutputStream file = new FileOutputStream(destination);
           StringWriter buffer = new StringWriter();
           out.Write(type, buffer);
           String text = buffer.ToString();
           byte[] octets = text.getBytes("UTF-8");
           System.out.Write(octets);
           System.out.flush();
           file.Write(octets);
           file.close();
           Validate(text);
           File domDestination = new File(directory, type.getClass().getSimpleName() + ".dom.xml");
           File asciiDestination = new File(directory, type.getClass().getSimpleName() + ".ascii-dom.xml");
           OutputStream domFile = new FileOutputStream(domDestination);
           OutputStream asciiFile = new FileOutputStream(asciiDestination);
           Writer asciiOut = new OutputStreamWriter(asciiFile, "iso-8859-1");
           /*
            * This DOM document is the result of serializing the object in to a
            * string. The document can then be used to validate serialization.
            */
           Document doc = builder.Parse(new InputSource(new StringReader(text)));
           transformer.setOutputProperty(OutputKeys.INDENT, "yes");
           transformer.transform(new DOMSource(doc), new StreamResult(domFile));
           transformer.transform(new DOMSource(doc), new StreamResult(asciiOut));
           domFile.close();
           asciiFile.close();
           out.Validate(type.getClass(), text);
           File hyphenFile = new File(directory, type.getClass().getSimpleName() + ".hyphen.xml");
           Strategy strategy = new CycleStrategy("ID", "REFERER");
           Visitor visitor = new DebugVisitor();
           strategy = new VisitorStrategy(visitor, strategy);
           Style style = new HyphenStyle();
           Format format = new Format(style);
           Persister hyphen = new Persister(strategy, format);
           hyphen.Write(type, hyphenFile);
           hyphen.Write(type, System.out);
           hyphen.Read(type.getClass(), hyphenFile);
           File camelCaseFile = new File(directory, type.getClass().getSimpleName() + ".camel-case.xml");
           Style camelCaseStyle = new CamelCaseStyle(true, false);
           Format camelCaseFormat = new Format(camelCaseStyle);
           Persister camelCase = new Persister(strategy, camelCaseFormat);
           camelCase.Write(type, camelCaseFile);
           camelCase.Write(type, System.out);
           camelCase.Read(type.getClass(), camelCaseFile);
       }
       public synchronized Document Parse(String text) {
          return builder.Parse(new InputSource(new StringReader(text)));
       }
       public synchronized void Validate(String text) {
           builder.Parse(new InputSource(new StringReader(text)));
           System.out.println(text);
       }
       public void AssertElementExists(String sourceXml, String pathExpression) {
          AssertMatch(sourceXml, pathExpression, new MatchAny(), true);
       }
       public void AssertElementHasValue(String sourceXml, String pathExpression, String value) {
          AssertMatch(sourceXml, pathExpression, new ElementMatch(value), true);
       }
       public void AssertElementHasCDATA(String sourceXml, String pathExpression, String value) {
          AssertMatch(sourceXml, pathExpression, new ElementCDATAMatch(value), true);
       }
       public void AssertElementHasAttribute(String sourceXml, String pathExpression, String name, String value) {
          AssertMatch(sourceXml, pathExpression, new AttributeMatch(name, value), true);
       }
       public void AssertElementHasNamespace(String sourceXml, String pathExpression, String reference) {
          AssertMatch(sourceXml, pathExpression, new OfficialNamespaceMatch(reference), true);
       }
       public void AssertElementDoesNotExist(String sourceXml, String pathExpression) {
          AssertMatch(sourceXml, pathExpression, new MatchAny(), false);
       }
       public void AssertElementDoesNotHaveValue(String sourceXml, String pathExpression, String value) {
          AssertMatch(sourceXml, pathExpression, new ElementMatch(value), false);
       }
       public void AssertElementDoesNotHaveCDATA(String sourceXml, String pathExpression, String value) {
          AssertMatch(sourceXml, pathExpression, new ElementCDATAMatch(value), false);
       }
       public void AssertElementDoesNotHaveAttribute(String sourceXml, String pathExpression, String name, String value) {
          AssertMatch(sourceXml, pathExpression, new AttributeMatch(name, value), false);
       }
       public void AssertElementDoesNotHaveNamespace(String sourceXml, String pathExpression, String reference) {
          AssertMatch(sourceXml, pathExpression, new OfficialNamespaceMatch(reference), false);
       }
       public void AssertMatch(String sourceXml, String pathExpression, ExpressionMatch match, bool assertTrue) {
          Document document = Parse(sourceXml);
          ExpressionMatcher matcher = new ExpressionMatcher(pathExpression, match);
          if(!assertTrue) {
             assertFalse("Document does have expression '"+pathExpression+"' with "+match.Description+" for "+sourceXml, matcher.Matches(document));
          } else {
             assertTrue("Document does not match expression '"+pathExpression+"' with "+match.Description+" for "+sourceXml, matcher.Matches(document));
          }
       }
       private static class ExpressionMatcher {
          private Pattern pattern;
          private String[] segments;
          private ExpressionMatch match;
          private String pathExpression;
          public ExpressionMatcher(String pathExpression, ExpressionMatch match) {
             this.segments = pathExpression.replaceAll("^\\/", "").split("\\/");
             this.pattern = Pattern.compile("^(.*)\\[([0-9]+)\\]$");
             this.pathExpression = pathExpression;
             this.match = match;
          }
          public bool Matches(Document document) {
             org.w3c.dom.Element element = document.getDocumentElement();
             if(!GetLocalPart(element).equals(segments[0])) {
                return false;
             }
             for(int i = 1; i < segments.length; i++) {
                Matcher matcher = pattern.matcher(segments[i]);
                String path = segments[i];
                int index = 0;
                if(matcher.Matches()) {
                   String value = matcher.group(2);
                   index = Integer.parseInt(value);
                   path = matcher.group(1);
                }
                List<org.w3c.dom.Element> list = getElementsByTagName(element, path);
                if(index >= list.size()) {
                   return false;
                }
                element = list.get(index);
                if(element == null) {
                   return false;
                }
             }
             return match.Match(element);
          }
          public String ToString() {
             return pathExpression;
          }
       }
       public static List<org.w3c.dom.Element> getElementsByTagName(org.w3c.dom.Element element, String name) {
          List<org.w3c.dom.Element> list = new ArrayList<org.w3c.dom.Element>();
          NodeList allElements = element.getElementsByTagName("*");
          for(int i = 0; i < allElements.getLength(); i++) {
             Node node = allElements.item(i);
             if(node instanceof org.w3c.dom.Element && node.getParentNode() == element) {
                org.w3c.dom.Element itemNode = (org.w3c.dom.Element)node;
                String localName = GetLocalPart(itemNode);
                if(localName.equals(name)) {
                   list.add(itemNode);
                }
             }
          }
          return list;
       }
       public String GetLocalPart(org.w3c.dom.Element element) {
          if(element != null) {
             String tagName = element.getTagName();
             if(tagName != null) {
                return tagName.replaceAll(".*:", "");
             }
          }
          return null;
       }
       public String GetPrefix(org.w3c.dom.Element element) {
          if(element != null) {
             String tagName = element.getTagName();
             if(tagName != null && tagName.Matches(".+:.+")) {
                return tagName.replaceAll(":.*", "");
             }
          }
          return null;
       }
       public static interface ExpressionMatch{
          public bool Match(org.w3c.dom.Element element);
          public abstract String Description {
             get;
          }
          //public String GetDescription();
       public static class MatchAny : ExpressionMatch {
          public bool Match(org.w3c.dom.Element element) {
             return element != null;
          }
          public String Description {
             get {
                return "path";
             }
          }
          //public String GetDescription() {
          //   return "path";
          //}
       public static class ElementMatch : ExpressionMatch {
          private readonly String text;
          public ElementMatch(String text){
             this.text = text;
          }
          public bool Match(org.w3c.dom.Element element) {
             if(element != null) {
                Node value = element.getFirstChild();
                return value != null && value.getNodeValue().equals(text);
             }
             return false;
          }
          public String Description {
             get {
                return "text value equal to '"+text+"'";
             }
          }
          //public String GetDescription() {
          //   return "text value equal to '"+text+"'";
          //}
       public static class ElementCDATAMatch : ExpressionMatch {
          private readonly String text;
          public ElementCDATAMatch(String text){
             this.text = text;
          }
          public bool Match(org.w3c.dom.Element element) {
             if(element != null) {
                Node value = element.getFirstChild();
                if(value instanceof CDATASection) {
                   return value != null && value.getNodeValue().equals(text);
                }
                return false;
             }
             return false;
          }
          public String Description {
             get {
                return "text value equal to '"+text+"'";
             }
          }
          //public String GetDescription() {
          //   return "text value equal to '"+text+"'";
          //}
       public static class NamespaceMatch : ExpressionMatch {
          private readonly String reference;
          public NamespaceMatch(String reference) {
             this.reference = reference;
          }
          public bool Match(org.w3c.dom.Element element) {
             if(element != null) {
                String prefix = GetPrefix(element); // a:element -> a
                if(prefix != null && prefix.equals("")) {
                   prefix = null;
                }
                return Match(element, prefix);
             }
             return false;
          }
          public bool Match(org.w3c.dom.Element element, String prefix) {
             if(element != null) {
                String currentPrefix = GetPrefix(element); // if prefix is null, then this is inherited
                if((currentPrefix != null && prefix == null) ) {
                   prefix = currentPrefix; // inherit parents
                }
                String name = "xmlns"; // default xmlns=<reference>
                if(prefix != null && !prefix.equals("")) {
                   name = name + ":" + prefix; // xmlns:a=<reference>
                }
                String value = element.getAttribute(name);
                if(value == null || value.equals("")) {
                   Node parent = element.getParentNode();
                   if(parent instanceof org.w3c.dom.Element) {
                      return Match((org.w3c.dom.Element)element.getParentNode(), prefix);
                   }
                }
                return value != null && value.equals(reference);
             }
             return false;
          }
          public String Description {
             get {
                return "namespace reference as '"+reference+"'";
             }
          }
          //public String GetDescription() {
          //   return "namespace reference as '"+reference+"'";
          //}
       public static class OfficialNamespaceMatch : ExpressionMatch {
          private readonly String reference;
          public OfficialNamespaceMatch(String reference) {
             this.reference = reference;
          }
          public bool Match(org.w3c.dom.Element element) {
             if(element != null) {
                String actual = element.getNamespaceURI();
                if(actual == null){
                   return reference == null || reference.equals("");
                }
                return element.getNamespaceURI().equals(reference);
             }
             return false;
          }
          public String Description {
             get {
                return "namespace reference as '"+reference+"'";
             }
          }
          //public String GetDescription() {
          //   return "namespace reference as '"+reference+"'";
          //}
       public static class AttributeMatch : ExpressionMatch {
          private readonly String name;
          private readonly String value;
          public AttributeMatch(String name, String value) {
             this.name = name;
             this.value = value;
          }
          public bool Match(org.w3c.dom.Element element) {
             if(element != null) {
                String attribute = element.getAttribute(name);
                return attribute != null && attribute.equals(value);
             }
             return false;
          }
          public String Description {
             get {
                return "attribute "+name+"='"+value+"'";
             }
          }
          //public String GetDescription() {
          //   return "attribute "+name+"='"+value+"'";
          //}
       public void Print(Node node) {
          Queue<Node> nodes = new LinkedList<Node>();
          nodes.add(node);
          while (!nodes.isEmpty()) {
            node = nodes.poll();
            NodeList list = node.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
               if(list.item(i) instanceof org.w3c.dom.Element) {
                  nodes.add(list.item(i));
               }
            }
            System.out.format("name='%s' prefix='%s' reference='%s'%n", node.GetPrefix(), node.getLocalName(),
                node.getNamespaceURI());
          }
        }
        public void DumpNamespaces(String xml) {
          DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
          dbf.setNamespaceAware(true);
          Document doc = dbf.newDocumentBuilder().Parse(
              new InputSource(new StringReader(xml)));
          Print(doc.getDocumentElement());
        }
        public static class DebugVisitor : Visitor {
           public void Read(Type type, NodeMap<InputNode> node) {
              InputNode element = node.getNode();
              if(element.isRoot()) {
                 Object source = element.getSource();
                 Class sourceType = source.getClass();
                 Class itemType = type.getType();
                 System.out.printf(">>>>> ELEMENT=[%s]%n>>>>> TYPE=[%s]%n>>>>> SOURCE=[%s]%n", element, itemType, sourceType);
              }
           }
           public void Write(Type type, NodeMap<OutputNode> node) {
              if(!node.getNode().isRoot()) {
                 node.getNode().setComment(type.getType().getName());
              }
           }
        }
   }
}
