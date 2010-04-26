#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class MixTest : ValidationTestCase {
      [Root]
      private static class MixExample {
         [ElementList]
         private List<Object> list;
         [ElementMap]
         private Map<Object, Object> map;
         [Element]
         private Calendar calendar;
         public MixExample() {
            this.list = new ArrayList();
            this.map = new HashMap();
         }
         public Date Time {
            set {
               calendar = new GregorianCalendar();
               calendar.Time = date;
            }
         }
         //public void SetTime(Date date) {
         //   calendar = new GregorianCalendar();
         //   calendar.Time = date;
         //}
            map.Put(key, value);
         }
         public Object Get(int index) {
            return list.Get(index);
         }
         public void Add(Object object) {
            list.Add(object);
         }
      }
      [Root]
      private static class Entry {
         [Attribute]
         private String id;
         [Text]
         private String text;
         public Entry() {
            super();
         }
         public Entry(String id, String text) {
            this.id = id;
            this.text = text;
         }
      }
      public void TestMix() {
         Serializer serializer = new Persister();
         MixExample example = new MixExample();
         StringWriter source = new StringWriter();
         example.setTime(new Date());
         example.Add("text");
         example.Add(1);
         example.Add(true);
         example.Add(new Entry("1", "example 1"));
         example.Add(new Entry("2", "example 2"));
         example.Put(new Entry("1", "key 1"), new Entry("1", "value 1"));
         example.Put("key 2", "value 2");
         example.Put("key 3", 3);
         example.Put("key 4", new Entry("4", "value 4"));
         serializer.write(example, System.out);
         serializer.write(example, source);
         serializer.validate(MixExample.class, source.toString());
         MixExample other = serializer.read(MixExample.class, source.toString());
         serializer.write(other, System.out);
         AssertEquals(example.Get(0), "text");
         AssertEquals(example.Get(1), 1);
         AssertEquals(example.Get(2), true);
         validate(example, serializer);
      }
   }
}
