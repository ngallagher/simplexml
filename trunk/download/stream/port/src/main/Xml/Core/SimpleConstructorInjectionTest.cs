#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class SimpleConstructorInjectionTest : TestCase {
      public void TestConstructorInjection() {
         String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
               "<MessageWrapper>" +
               "<necessary>test</necessary>" +
               "<optional/>" +
               "</MessageWrapper>";
         Serializer serializer = new Persister();
         Message example = serializer.read(Message.class, xml);
         System.out.println("message: "+example.Optional);
      }
      [Root]
      public static class Message : Serializable{
         [Element]
         private String necessary;
         [Element(Required=false)]
         private String optional;
         public Message(@Element(name="necessary") String necessary){
            this.necessary = necessary;
         }
         public String Necessary {
            get {
               return necessary;
            }
         }
         //public String GetNecessary() {
         //   return necessary;
         //}
            this.necessary = necessary;
         }
         public String Optional {
            get {
               return optional;
            }
         }
         //public String GetOptional() {
         //   return optional;
         //}
            this.optional = optional;
         }
      }
   }
}
