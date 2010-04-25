#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   import org.w3c.dom.Document;
   public class AliasTest : ValidationTestCase {
       public class AliasStrategy : Strategy {
           private readonly Strategy strategy;
           private readonly Map<Class, String> forward;
           private readonly Map<String, Class> backward;
           private AliasStrategy(Strategy strategy) {
               this.forward = new ConcurrentHashMap<Class, String>();
               this.backward = new  ConcurrentHashMap<String, Class>();
               this.strategy = strategy;
           }
           public void AddAlias(Class type, String name) {
               forward.put(type, name);
               backward.put(name, type);
           }
           public Value Read(Type field, NodeMap<InputNode> node, Dictionary map) {
               Node entry = node.remove("type");
               if(entry != null) {
                   String value = entry.getValue();
                   Class type = backward.Get(value);
                   if(type == null) {
                       throw new PersistenceException("Could not find class for alias %s", value);
                   }
                   node.put("class", type.getName());
               }
               return strategy.Read(field, node, map);
           }
           public bool Write(Type field, Object value, NodeMap<OutputNode> node, Dictionary map) {
               bool done = strategy.Write(field, value, node, map);
               Node entry = node.remove("class");
               if(entry != null) {
                   String className = entry.getValue();
                   Class type = Class.forName(className);
                   String name = forward.Get(type);
                   if(name == null) {
                       throw new PersistenceException("Could not find alias for class %s", className);
                   }
                   node.put("type", name);
               }
               return done;
           }
       }
       @Root
       [Namespace(Prefix="table", Reference="http://simpleframework.org/map")]
       private static class MultiValueMap {
           @ElementMap
           private Map<String, Object> map;
           public MultiValueMap() {
               this.map = new HashMap<String, Object>();
           }
           public void Add(String name, Object value) {
               map.put(name, value);
           }
           public Object Get(String name) {
               return map.Get(name);
           }
       }
       @Root
       [Namespace(Prefix="item", Reference="http://simpleframework.org/entry")]
       private static class MultiValueEntry {
           [Attribute(Name="name")]
           private String name;
           [Element(Name="value")]
           private String value;
           public MultiValueEntry(@Attribute(name="name") String name,
                                   [Element(Name="value") String value)]
               this.name = name;
               this.value = value;
           }
       }
       public void TestMap() {
           Strategy strategy = new TreeStrategy();
           AliasStrategy alias = new AliasStrategy(strategy);
           Persister persister = new Persister(alias);
           MultiValueMap map = new MultiValueMap();
           alias.AddAlias(HashMap.class, "map");
           alias.AddAlias(Integer.class, "int");
           alias.AddAlias(Double.class, "float");
           alias.AddAlias(String.class, "text");
           alias.AddAlias(MultiValueEntry.class, "item");
           map.Add("integer", 1);
           map.Add("double", 0.0d);
           map.Add("string", "test");
           map.Add("item", new MultiValueEntry("example", "item"));
           StringWriter out = new StringWriter();
           persister.Write(map, out);
           String text = out.toString();//.replaceAll("entry", "table:entry");
           System.err.println(text);
           MultiValueMap read = persister.Read(MultiValueMap.class, text);
           assertEquals(read.Get("integer"), 1);
           assertEquals(read.Get("double"), 0.0d);
           assertEquals(read.Get("string"), "test");
           validate(persister, map);
           DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
           // Ensure we know about namespaces
           factory.setNamespaceAware(true);
           factory.setValidating(false);
           DocumentBuilder builder = factory.newDocumentBuilder();
           StringReader reader = new StringReader(text);
           InputSource source = new InputSource(reader);
           Document doc = builder.parse(source);
           org.w3c.dom.Element element = doc.getDocumentElement();
           assertEquals("multiValueMap", element.getLocalName());
           assertEquals("http://simpleframework.org/map", element.getNamespaceURI());
       }
   }
}
