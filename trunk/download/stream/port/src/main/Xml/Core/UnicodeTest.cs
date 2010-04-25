#region Using directives
using SimpleFramework.Xml.Util;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class UnicodeTest : ValidationTestCase {
      private const String SOURCE =
      "<?xml version='1.0' encoding='UTF-8'?>\n"+
      "<example>\n"+
      "   <list>\n"+
      "      <unicode origin=\"Australia\" name=\"Nicole Kidman\">\n"+
      "         <text>Nicole Kidman</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Austria\" name=\"Johann Strauss\">\n"+
      "         <text>Johann Strauß</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Canada\" name=\"Celine Dion\">\n"+
      "         <text>Céline Dion</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Democratic People's Rep. of Korea\" name=\"LEE Sol-Hee\">\n"+
      "         <text>이설희</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Denmark\" name=\"Soren Hauch-Fausboll\">\n"+
      "         <text>Søren Hauch-Fausbøll</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Denmark\" name=\"Soren Kierkegaard\">\n"+
      "         <text>Søren Kierkegård</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Egypt\" name=\"Abdel Halim Hafez\">\n"+
      "         <text>ﻋﺑﺪﺍﻠﺣﻟﻳﻢ ﺤﺎﻓﻅ</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Egypt\" name=\"Om Kolthoum\">\n"+
      "         <text>ﺃﻡ ﻛﻟﺛﻭﻡ</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Eritrea\" name=\"Berhane Zeray\">\n"+
      "         <text>ኤርትራ</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Ethiopia\" name=\"Haile Gebreselassie\">\n"+
      "         <text>ኢትዮጵያ</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"France\" name=\"Gerard Depardieu\">\n"+
      "         <text>Gérard Depardieu</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"France\" name=\"Jean Reno\">\n"+
      "         <text>Jean Réno</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"France\" name=\"Camille Saint-Saens\">\n"+
      "         <text>Camille Saint-Saëns</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"France\" name=\"Mylene Demongeot\">\n"+
      "         <text>Mylène Demongeot</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"France\" name=\"Francois Truffaut\">\n"+
      "         <text>François Truffaut</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Germany\" name=\"Rudi Voeller\">\n"+
      "         <text>Rudi Völler</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Germany\" name=\"Walter Schultheiss\">\n"+
      "         <text>Walter Schultheiß</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Greece\" name=\"Giorgos Dalaras\">\n"+
      "         <text>Γιώργος Νταλάρας</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Iceland\" name=\"Bjork Gudmundsdottir\">\n"+
      "         <text>Björk Guðmundsdóttir</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"India (Hindi)\" name=\"Madhuri Dixit\">\n"+
      "         <text>माधुरी दिछित</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Ireland\" name=\"Sinead O'Connor\">\n"+
      "         <text>Sinéad O'Connor</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Israel\" name=\"Yehoram Gaon\">\n"+
      "         <text>יהורם גאון</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Italy\" name=\"Fabrizio DeAndre\">\n"+
      "         <text>Fabrizio De André</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Japan\" name=\"KUBOTA Toshinobu\">\n"+
      "         <text>久保田    利伸</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Japan\" name=\"HAYASHIBARA Megumi\">\n"+
      "         <text>林原 めぐみ</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Japan\" name=\"Mori Ogai\">\n"+
      "         <text>森鷗外</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Japan\" name=\"Tex Texin\">\n"+
      "         <text>テクス テクサン</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Norway\" name=\"Tor Age Bringsvaerd\">\n"+
      "         <text>Tor Åge Bringsværd</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Pakistan (Urdu)\" name=\"Nusrat Fatah Ali Khan\">\n"+
      "         <text>نصرت فتح علی خان</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"People's Rep. of China\" name=\"ZHANG Ziyi\">\n"+
      "         <text>章子怡</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"People's Rep. of China\" name=\"WONG Faye\">\n"+
      "         <text>王菲</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Poland\" name=\"Lech Walesa\">\n"+
      "         <text>Lech Wałęsa</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Puerto Rico\" name=\"Olga Tanon\">\n"+
      "         <text>Olga Tañón</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Rep. of China\" name=\"Hsu Chi\">\n"+
      "         <text>舒淇</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Rep. of China\" name=\"Ang Lee\">\n"+
      "         <text>李安</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Rep. of Korea\" name=\"AHN Sung-Gi\">\n"+
      "         <text>안성기</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Rep. of Korea\" name=\"SHIM Eun-Ha\">\n"+
      "         <text>심은하</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Russia\" name=\"Mikhail Gorbachev\">\n"+
      "         <text>Михаил Горбачёв</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Russia\" name=\"Boris Grebenshchikov\">\n"+
      "         <text>Борис Гребенщиков</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Syracuse (Sicily)\" name=\"Archimedes\">\n"+
      "         <text>Ἀρχιμήδης</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"Thailand\" name=\"Thongchai McIntai\">\n"+
      "         <text>ธงไชย แม็คอินไตย์</text>\n"+
      "      </unicode>\n"+
      "      <unicode origin=\"U.S.A.\" name=\"Brad Pitt\">\n"+
      "         <text>Brad Pitt</text>\n"+
      "      </unicode>\n"+
      "   </list>\n"+
      "</example>\n";
      [Root(Name="unicode")]
      private static class Unicode : Entry {
         [Attribute(Name="origin")]
         private String origin;
         [Element(Name="text")]
         private String text;
         @Attribute
         private String name;
         public String Name {
            get {
               return name;
            }
         }
         //public String GetName() {
         //   return name;
         //}
      [Root(Name="example")]
      private static class UnicodeExample {
         [ElementList(Name="list", Type=Unicode.class)]
         private Dictionary<Unicode> list;
         public Unicode Get(String name) {
            return list.Get(name);
         }
      }
      private Persister persister;
      public void SetUp() {
         persister = new Persister();
      }
      public void TestUnicode() {
         UnicodeExample example = persister.read(UnicodeExample.class, SOURCE);
         AssertUnicode(example);
         validate(example, persister); // Ensure the deserialized object is valid
      }
      public void TestWriteUnicode() {
         UnicodeExample example = persister.read(UnicodeExample.class, SOURCE);
         AssertUnicode(example);
         validate(example, persister); // Ensure the deserialized object is valid
         StringWriter out = new StringWriter();
         persister.write(example, out);
         example = persister.read(UnicodeExample.class, out.toString());
         AssertUnicode(example);
         validate(example, persister);
      }
      public void TestUnicodeFromByteStream() {
         byte[] data = SOURCE.getBytes("UTF-8");
         InputStream source = new ByteArrayInputStream(data);
         UnicodeExample example = persister.read(UnicodeExample.class, source);
         AssertUnicode(example);
         validate(example, persister); // Ensure the deserialized object is valid
      }
      public void TestIncorrectEncoding() {
         byte[] data = SOURCE.getBytes("UTF-8");
         InputStream source = new ByteArrayInputStream(data);
         UnicodeExample example = persister.read(UnicodeExample.class, new InputStreamReader(source, "ISO-8859-1"));
         assertFalse("Encoding of ISO-8859-1 did not work", IsUnicode(example));
      }
      public void AssertUnicode(UnicodeExample example) {
         assertTrue("Data was not unicode", IsUnicode(example));
      }
      public bool IsUnicode(UnicodeExample example) {
         // Ensure text remailed unicode
         if(!example.Get("Nicole Kidman").text.equals("Nicole Kidman")) return false;
         if(!example.Get("Johann Strauss").text.equals("Johann Strauß")) return false;
         if(!example.Get("Celine Dion").text.equals("Céline Dion")) return false;
         if(!example.Get("LEE Sol-Hee").text.equals("이설희")) return false;
         if(!example.Get("Soren Hauch-Fausboll").text.equals("Søren Hauch-Fausbøll")) return false;
         if(!example.Get("Soren Kierkegaard").text.equals("Søren Kierkegård")) return false;
         if(!example.Get("Abdel Halim Hafez").text.equals("ﻋﺑﺪﺍﻠﺣﻟﻳﻢ ﺤﺎﻓﻅ")) return false;
         if(!example.Get("Om Kolthoum").text.equals("ﺃﻡ ﻛﻟﺛﻭﻡ")) return false;
         if(!example.Get("Berhane Zeray").text.equals("ኤርትራ")) return false;
         if(!example.Get("Haile Gebreselassie").text.equals("ኢትዮጵያ")) return false;
         if(!example.Get("Gerard Depardieu").text.equals("Gérard Depardieu")) return false;
         if(!example.Get("Jean Reno").text.equals("Jean Réno")) return false;
         if(!example.Get("Camille Saint-Saens").text.equals("Camille Saint-Saëns")) return false;
         if(!example.Get("Mylene Demongeot").text.equals("Mylène Demongeot")) return false;
         if(!example.Get("Francois Truffaut").text.equals("François Truffaut")) return false;
         //if(!example.Get("Rudi Voeller").text.equals("Rudi Völler")) return false;
         if(!example.Get("Walter Schultheiss").text.equals("Walter Schultheiß")) return false;
         if(!example.Get("Giorgos Dalaras").text.equals("Γιώργος Νταλάρας")) return false;
         if(!example.Get("Bjork Gudmundsdottir").text.equals("Björk Guðmundsdóttir")) return false;
         if(!example.Get("Madhuri Dixit").text.equals("माधुरी दिछित")) return false;
         if(!example.Get("Sinead O'Connor").text.equals("Sinéad O'Connor")) return false;
         if(!example.Get("Yehoram Gaon").text.equals("יהורם גאון")) return false;
         if(!example.Get("Fabrizio DeAndre").text.equals("Fabrizio De André")) return false;
         if(!example.Get("KUBOTA Toshinobu").text.equals("久保田    利伸")) return false;
         if(!example.Get("HAYASHIBARA Megumi").text.equals("林原 めぐみ")) return false;
         if(!example.Get("Mori Ogai").text.equals("森鷗外")) return false;
         if(!example.Get("Tex Texin").text.equals("テクス テクサン")) return false;
         if(!example.Get("Tor Age Bringsvaerd").text.equals("Tor Åge Bringsværd")) return false;
         if(!example.Get("Nusrat Fatah Ali Khan").text.equals("نصرت فتح علی خان")) return false;
         if(!example.Get("ZHANG Ziyi").text.equals("章子怡")) return false;
         if(!example.Get("WONG Faye").text.equals("王菲")) return false;
         if(!example.Get("Lech Walesa").text.equals("Lech Wałęsa")) return false;
         if(!example.Get("Olga Tanon").text.equals("Olga Tañón")) return false;
         if(!example.Get("Hsu Chi").text.equals("舒淇")) return false;
         if(!example.Get("Ang Lee").text.equals("李安")) return false;
         if(!example.Get("AHN Sung-Gi").text.equals("안성기")) return false;
         if(!example.Get("SHIM Eun-Ha").text.equals("심은하")) return false;
         if(!example.Get("Mikhail Gorbachev").text.equals("Михаил Горбачёв")) return false;
         if(!example.Get("Boris Grebenshchikov").text.equals("Борис Гребенщиков")) return false;
         if(!example.Get("Archimedes").text.equals("Ἀρχιμήδης")) return false;
         if(!example.Get("Thongchai McIntai").text.equals("ธงไชย แม็คอินไตย์")) return false;
         if(!example.Get("Brad Pitt").text.equals("Brad Pitt")) return false;
         return true;
      }
   }
}
