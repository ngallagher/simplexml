#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class WrapperTest : TestCase {
      private static class Wrapper {
         private readonly Object value;
         public Wrapper(Object value) {
            this.value = value;
         }
         public Object Get() {
            return value;
         }
      }
      private static class WrapperConverter : Converter<Wrapper> {
         private readonly Serializer serializer;
         public WrapperConverter(Serializer serializer) {
            this.serializer = serializer;
         }
         public Wrapper Read(InputNode node) {
            InputNode type = node.getAttribute("type");
            InputNode child = node.getNext();
            String className = type.getValue();
            Object value = null;
            if(child != null) {
               value = serializer.Read(Class.forName(className), child);
            }
            return new Wrapper(value);
         }
         public void Write(OutputNode node, Wrapper wrapper) {
            Object value = wrapper.;
            Class type = value.getClass();
            String className = type.getName();
            node.setAttribute("type", className);
            serializer.Write(value, node);
         }
      }
      @Root
      @Default
      private static class Entry {
         private String name;
         private String value;
         public Entry(@Element(name="name", required=false) String name, @Element(name="value", required=false) String value){
            this.name = name;
            this.value = value;
         }
      }
      @Root
      @Default
      private static class WrapperExample {
         @Convert(WrapperConverter.class)
         private Wrapper wrapper;
         public WrapperExample(@Element(name="wrapper", required=false) Wrapper wrapper) {
            this.wrapper = wrapper;
         }
      }
      public void TestWrapper() {
         Registry registry = new Registry();
         Strategy strategy = new RegistryStrategy(registry);
         Serializer serializer = new Persister(strategy);
         Entry entry = new Entry("name", "value");
         Wrapper wrapper = new Wrapper(entry);
         WrapperExample example = new WrapperExample(wrapper);
         WrapperConverter converter = new WrapperConverter(serializer);
         StringWriter writer = new StringWriter();
         registry.bind(Wrapper.class, converter);
         serializer.Write(example, writer);
         serializer.Read(WrapperExample.class, writer.toString());
         System.err.println(writer.toString());
      }
   }
}
