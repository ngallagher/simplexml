#region Using directives
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Stream {
   public class StyleTest : TestCase {
      public void TestHyphenStyle() {
         Style strategy = new HyphenStyle();
         Console.WriteLine(strategy.GetElement("Base64Encoder"));
         Console.WriteLine(strategy.GetElement("Base64_Encoder"));
         Console.WriteLine(strategy.GetElement("Base64___encoder"));
         Console.WriteLine(strategy.GetElement("base64--encoder"));
         Console.WriteLine(strategy.GetElement("Base64encoder"));
         Console.WriteLine(strategy.GetElement("_Base64encoder"));
         Console.WriteLine(strategy.GetElement("__Base64encoder"));
         Console.WriteLine(strategy.GetElement("URLList"));
         Console.WriteLine(strategy.GetElement("__Base64encoder"));
         Console.WriteLine(strategy.GetElement("Base_64_Encoder"));
         Console.WriteLine(strategy.GetElement("base_64_encoder"));
      }
      public void TestCamelCaseStyle() {
         Style strategy = new CamelCaseStyle();
         Console.WriteLine(strategy.GetElement("Base64Encoder"));
         Console.WriteLine(strategy.GetElement("Base64_Encoder"));
         Console.WriteLine(strategy.GetElement("Base64___encoder"));
         Console.WriteLine(strategy.GetElement("base64--encoder"));
         Console.WriteLine(strategy.GetElement("Base64encoder"));
         Console.WriteLine(strategy.GetElement("_Base64encoder"));
         Console.WriteLine(strategy.GetElement("__Base64encoder"));
         Console.WriteLine(strategy.GetElement("URLList"));
         Console.WriteLine(strategy.GetElement("__Base64encoder"));
         Console.WriteLine(strategy.GetElement("Base_64_Encoder"));
         Console.WriteLine(strategy.GetElement("base_64_encoder"));
      }
   }
}
