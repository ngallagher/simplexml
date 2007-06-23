package simple.xml.load;

import java.io.FileOutputStream;
import java.io.StringWriter;
import java.util.List;

import simple.xml.util.Entry;
import simple.xml.util.Dictionary;
import simple.xml.ValidationTestCase;
import simple.xml.ElementList;
import simple.xml.Attribute;
import simple.xml.Element;
import simple.xml.Root;

public class UnicodeTest extends ValidationTestCase {

   private static final String SOURCE =        
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
   
   @Root(name="unicode")
   private static class Unicode extends Entry {

      @Attribute(name="origin")
      private String origin;

      @Element(name="text")
      private String text;
   }

   @Root(name="example")
   private static class UnicodeExample {

      @ElementList(name="list", type=Unicode.class)
      private Dictionary<Unicode> list;

      public Unicode get(String name) {
         return list.get(name);              
      }
   }

   private Persister persister;

   public void setUp() throws Exception {
      persister = new Persister();
   }
	
   public void testUnicode() throws Exception {    
      UnicodeExample example = persister.read(UnicodeExample.class, SOURCE);

      assertUnicode(example);
      validate(example, persister); // Ensure the deserialized object is valid
   }

   public void testWriteUnicode() throws Exception {
      UnicodeExample example = persister.read(UnicodeExample.class, SOURCE);

      assertUnicode(example);
      validate(example, persister); // Ensure the deserialized object is valid

      StringWriter out = new StringWriter();
      persister.write(example, out);
      example = persister.read(UnicodeExample.class, out.toString());

      assertUnicode(example);
      validate(example, persister);      
   }


   public void assertUnicode(UnicodeExample example) throws Exception {
      // Ensure text remailed unicode           
      assertEquals(example.get("Nicole Kidman").text, "Nicole Kidman");
      assertEquals(example.get("Johann Strauss").text, "Johann Strauß");
      assertEquals(example.get("Celine Dion").text, "Céline Dion");
      assertEquals(example.get("LEE Sol-Hee").text, "이설희");
      assertEquals(example.get("Soren Hauch-Fausboll").text, "Søren Hauch-Fausbøll");
      assertEquals(example.get("Soren Kierkegaard").text, "Søren Kierkegård");
      assertEquals(example.get("Abdel Halim Hafez").text, "ﻋﺑﺪﺍﻠﺣﻟﻳﻢ ﺤﺎﻓﻅ");
      assertEquals(example.get("Om Kolthoum").text, "ﺃﻡ ﻛﻟﺛﻭﻡ");
      assertEquals(example.get("Berhane Zeray").text, "ኤርትራ");
      assertEquals(example.get("Haile Gebreselassie").text, "ኢትዮጵያ");
      assertEquals(example.get("Gerard Depardieu").text, "Gérard Depardieu");
      assertEquals(example.get("Jean Reno").text, "Jean Réno");
      assertEquals(example.get("Camille Saint-Saens").text, "Camille Saint-Saëns");
      assertEquals(example.get("Mylene Demongeot").text, "Mylène Demongeot");
      assertEquals(example.get("Francois Truffaut").text, "François Truffaut");
      //assertEquals(example.get("Rudi Voeller").text, "Rudi Völler");
      assertEquals(example.get("Walter Schultheiss").text, "Walter Schultheiß");
      assertEquals(example.get("Giorgos Dalaras").text, "Γιώργος Νταλάρας");
      assertEquals(example.get("Bjork Gudmundsdottir").text, "Björk Guðmundsdóttir");
      assertEquals(example.get("Madhuri Dixit").text, "माधुरी दिछित");
      assertEquals(example.get("Sinead O'Connor").text, "Sinéad O'Connor");
      assertEquals(example.get("Yehoram Gaon").text, "יהורם גאון");
      assertEquals(example.get("Fabrizio DeAndre").text, "Fabrizio De André");
      assertEquals(example.get("KUBOTA Toshinobu").text, "久保田    利伸");
      assertEquals(example.get("HAYASHIBARA Megumi").text, "林原 めぐみ");
      assertEquals(example.get("Mori Ogai").text, "森鷗外");
      assertEquals(example.get("Tex Texin").text, "テクス テクサン");
      assertEquals(example.get("Tor Age Bringsvaerd").text, "Tor Åge Bringsværd");
      assertEquals(example.get("Nusrat Fatah Ali Khan").text, "نصرت فتح علی خان");
      assertEquals(example.get("ZHANG Ziyi").text, "章子怡");
      assertEquals(example.get("WONG Faye").text, "王菲");
      assertEquals(example.get("Lech Walesa").text, "Lech Wałęsa");
      assertEquals(example.get("Olga Tanon").text, "Olga Tañón");
      assertEquals(example.get("Hsu Chi").text, "舒淇");
      assertEquals(example.get("Ang Lee").text, "李安");
      assertEquals(example.get("AHN Sung-Gi").text, "안성기");
      assertEquals(example.get("SHIM Eun-Ha").text, "심은하");
      assertEquals(example.get("Mikhail Gorbachev").text, "Михаил Горбачёв");
      assertEquals(example.get("Boris Grebenshchikov").text, "Борис Гребенщиков");
      assertEquals(example.get("Archimedes").text, "Ἀρχιμήδης");
      assertEquals(example.get("Thongchai McIntai").text, "ธงไชย แม็คอินไตย์");
      assertEquals(example.get("Brad Pitt").text, "Brad Pitt");
   }
}
