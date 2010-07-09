package com.rbsfm.plugin.build.patch;
import java.util.List;

import org.junit.Test;
import org.simpleframework.xml.core.Persister;
public class JardescTest {
   private static final String SOURCE =
   "<jardesc>\n" +
   "    <selectedElements exportClassFiles='true' exportJavaFiles='true' exportOutputFolder='false'>\n" +
   "        <file path='/cpbuild/config/templates/pricing.xml'/>\n" +
   "        <javaElement handleIdentifier='=bet/src&lt;com.rbsfm.fi.bet.autoquoter.business.step{AQSetBrokerCode.java'/>\n" +
   "        <javaElement handleIdentifier='=ficommon/src&lt;com.rbsfm.fi.pricing.external.detail{ExternalDetailsAdapter.java'/>\n" +
   "        <javaElement handleIdentifier='=external/src&lt;com.rbsfm.external.fi.pricing.detail.xml{ExternalBondStaticDetailsXmlAdapter.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{CouponAmountBB.java'/>\n" +
   "        <javaElement handleIdentifier='=fiecn/src&lt;com.rbsfm.fi.pricing.ecn.external.attribute{AutoQuoterAttributeNames.java'/>\n" +
   "        <javaElement handleIdentifier='=ficommon/src&lt;com.rbsfm.fi.pricing.mux.failover{ServerAvailabilityScanner.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{IndexMapper.java'/>\n" +
   "        <javaElement handleIdentifier='=bet/src&lt;com.rbsfm.fi.bet.autoquoter.business.conversation{UnknownSecConversationFactory.java'/>\n" +
   "        <javaElement handleIdentifier='=commonx/src&lt;com.rbsfm.commonx.fi.pricing.detail.rv{ExternalBondStaticDetailsRvAdapter.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{BondTypeRefBB.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{CashFlowBB.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{StaticMapper.java'/>\n" +
   "        <javaElement handleIdentifier='=external/src&lt;com.rbsfm.external.fi.pricing.detail.xml{ExternalInstrumentDetailsXmlScanner.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.instmaint.persist{StoredProc.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{AbstractFieldTranslator.java'/>\n" +
   "        <javaElement handleIdentifier='=ficommon/src&lt;com.rbsfm.fi.pricing.external.attribute{AttributeNames.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.pricing.dependency.simple{SimpleTreeDetailsProvider.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.instmaint.enrich{CeemeaBondTypeRefEnricher.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{ConditionalFieldTranslator.java'/>\n" +
   "        <javaElement handleIdentifier='=ficommon/src&lt;com.rbsfm.fi.pricing.newanalytics.detail.caf.proxy{CafHelperFunctions.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.pricing.dependency.util{SelectionListHelper.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{ConcatenatingFieldTranslator.java'/>\n" +
   "        <javaElement handleIdentifier='=ficommon/src&lt;com.rbsfm.fi.pricing.external.detail.mux{ExternalBondStaticMessage.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{ScheduleBB.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator.transformer'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.maintenance{StaticTranslator.java'/>\n" +
   "        <javaElement handleIdentifier='=egpricing/src&lt;com.rbsfm.fi.staticdata.translator{SimpleFieldTranslator.java'/>\n" +
   "        <javaElement handleIdentifier='=external/src&lt;com.rbsfm.external.fi.pricing.detail{ExternalBondStaticDetails.java'/>\n" +
   "    </selectedElements>\n" +
   "</jardesc>\n";      
   @Test
   public void read() throws Exception {
      Persister persister = new Persister();
      Jardesc jardesc = persister.read(Jardesc.class, SOURCE, false);
      List<SourceIdentifier> identifiers = jardesc.getSourceFiles();
      for(SourceIdentifier source : identifiers) {
         System.out.printf("[%s] [%s] %s%n",source.getProject(),source.getFolder(),source.getFile());
      }
      List<SourceIdentifier> files = jardesc.getFiles();
      for(SourceIdentifier file : files) {
         System.out.printf("[%s] [%s] %s%n",file.getProject(),file.getFolder(),file.getFile());
      }
   }
}
