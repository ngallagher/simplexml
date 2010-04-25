#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Util {
   public class CombinedStrategyTest : ValidationTestCase {
      private static class Item {
         private int value;
         public Item(int value) {
            this.value = value;
         }
         public int Value {
            get {
               return value;
            }
         }
         //public int GetValue() {
         //   return value;
         //}
      [Root]
      [Convert(ExtendedItemConverter.class)]
      private static class ExtendedItem : Item {
         public ExtendedItem(int value) {
            super(value);
         }
      }
      private static class AnnotationItemConverter : Converter<Item> {
         public Item Read(InputNode node) {
            return new Item(Integer.parseInt(node.getAttribute("value").Value));
         }
         public void Write(OutputNode node, Item value) {
            node.setAttribute("value", String.valueOf(value.Value));
            node.setAttribute("type", getClass().getName());
         }
      }
      private static class RegistryItemConverter : Converter<Item> {
         public Item Read(InputNode node) {
            return new Item(Integer.parseInt(node.getNext().Value));
         }
         public void Write(OutputNode node, Item value) {
            node.getChild("value").setValue(String.valueOf(value.Value));
            node.getChild("type").setValue(getClass().getName());
         }
      }
      private static class ExtendedItemConverter : Converter<ExtendedItem> {
         public ExtendedItem Read(InputNode node) {
            return new ExtendedItem(Integer.parseInt(node.getAttribute("value").Value));
         }
         public void Write(OutputNode node, ExtendedItem value) {
            node.setAttribute("value", String.valueOf(value.Value));
            node.setAttribute("type", getClass().getName());
         }
      }
      [Root]
      private static class CombinationExample {
         [Element]
         private Item item; // handled by the registry
         [Element]
         [Convert(AnnotationItemConverter.class)]
         private Item overriddenItem;
         [Element]
         private Item extendedItem; // handled by class annotation
         public CombinationExample(int item, int overriddenItem, int extendedItem) {
            this.item = new Item(item);
            this.overriddenItem = new Item(overriddenItem);
            this.extendedItem = new ExtendedItem(extendedItem);
         }
         public Item Item {
            get {
               return item;
            }
         }
         //public Item GetItem() {
         //   return item;
         //}
            return overriddenItem;
         }
         public Item ExtendedItem {
            get {
               return extendedItem;
            }
         }
         //public Item GetExtendedItem() {
         //   return extendedItem;
         //}
      public void TestCombinedStrategy() {
         Registry registry = new Registry();
         AnnotationStrategy annotationStrategy = new AnnotationStrategy();
         RegistryStrategy registryStrategy = new RegistryStrategy(registry, annotationStrategy);
         Persister persister = new Persister(registryStrategy);
         CombinationExample example = new CombinationExample(1, 2, 3);
         StringWriter writer = new StringWriter();
         registry.bind(Item.class, RegistryItemConverter.class);
         persister.Write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         assertElementExists(text, "/combinationExample/item/value");
         assertElementHasValue(text, "/combinationExample/item/value", "1");
         assertElementHasValue(text, "/combinationExample/item/type", RegistryItemConverter.class.getName());
         assertElementExists(text, "/combinationExample/overriddenItem");
         assertElementHasAttribute(text, "/combinationExample/overriddenItem", "value", "2");
         assertElementHasAttribute(text, "/combinationExample/overriddenItem", "type", AnnotationItemConverter.class.getName());
         assertElementExists(text, "/combinationExample/extendedItem");
         assertElementHasAttribute(text, "/combinationExample/extendedItem", "value", "3");
         assertElementHasAttribute(text, "/combinationExample/extendedItem", "type", ExtendedItemConverter.class.getName());
      }
      public void TestCombinationStrategyWithStyle() {
         Registry registry = new Registry();
         AnnotationStrategy annotationStrategy = new AnnotationStrategy();
         RegistryStrategy registryStrategy = new RegistryStrategy(registry, annotationStrategy);
         Style style = new HyphenStyle();
         Format format = new Format(style);
         Persister persister = new Persister(registryStrategy, format);
         CombinationExample example = new CombinationExample(1, 2, 3);
         StringWriter writer = new StringWriter();
         registry.bind(Item.class, RegistryItemConverter.class);
         persister.Write(example, writer);
         String text = writer.toString();
         System.out.println(text);
         assertElementExists(text, "/combination-example/item/value");
         assertElementHasValue(text, "/combination-example/item/value", "1");
         assertElementHasValue(text, "/combination-example/item/type", RegistryItemConverter.class.getName());
         assertElementExists(text, "/combination-example/overridden-item");
         assertElementHasAttribute(text, "/combination-example/overridden-item", "value", "2");
         assertElementHasAttribute(text, "/combination-example/overridden-item", "type", AnnotationItemConverter.class.getName());
         assertElementExists(text, "/combination-example/extended-item");
         assertElementHasAttribute(text, "/combination-example/extended-item", "value", "3");
         assertElementHasAttribute(text, "/combination-example/extended-item", "type", ExtendedItemConverter.class.getName());
      }
   }
}
