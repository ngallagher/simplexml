#region Using directives
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class StackOverflowTest : ValidationTestCase {
      private const int ITERATIONS = 1000;
      private const String NEW_BENEFIT =
      "<newBenefit>"+
      "   <office>AAAAA</office>"+
      "   <recordNumber>1046</recordNumber>"+
      "   <type>A</type>"+
      "</newBenefit>";
      private const String BENEFIT_MUTATION =
      "<benefitMutation>"+
      "   <office>AAAAA</office>"+
      "   <recordNumber>1046</recordNumber>"+
      "   <type>A</type>"+
      "   <comment>comment</comment>"+
      "</benefitMutation>";
      [Root]
      public static class Delivery {
         [ElementList(Inline = true, Required = false, Name = "newBenefit")]
         private List<NewBenefit> listNewBenefit = new ArrayList<NewBenefit>();
         [ElementList(Inline = true, Required = false, Name = "benefitMutation")]
         private List<BenefitMutation> listBenefitMutation = new ArrayList<BenefitMutation>();
      }
      public static class NewBenefit {
         [Element]
         private String office;
         [Element]
         private String recordNumber;
         [Element]
         private String type;
      }
      public static class BenefitMutation : NewBenefit {
         [Element(Required = false)]
         private String comment;
      }
      public void TestStackOverflow() {
         StringBuilder builder = new StringBuilder();
         Persister persister = new Persister();
         builder.append("<delivery>");
         bool isNewBenefit = true;
         for(int i = 0; i < ITERATIONS; i++) {
            String text = null;
            if(isNewBenefit) {
               text = NEW_BENEFIT;
            } else {
               text = BENEFIT_MUTATION;
            }
            isNewBenefit = !isNewBenefit ;
            builder.append(text);
         }
         builder.append("</delivery>");
         Delivery delivery = persister.read(Delivery.class, builder.toString());
         assertEquals(delivery.listBenefitMutation.size() + delivery.listNewBenefit.size(), ITERATIONS);
         validate(persister, delivery);
      }
   }
}
