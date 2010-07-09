package com.rbsfm.plugin.build.patch;
import org.junit.Test;
import org.simpleframework.xml.core.Persister;
public class ProjectTest {
   @Test
   public void write() throws Exception {
      Persister persister = new Persister();
      Project project = new Project("ceemea-2009WK52-22-patch-001");
      project.addProperty("config.group", "rates" );
      project.addProperty("scm.tag", "ceemea-2009WK52-22" );
      project.addProperty("patch.host.dir", "\\\\lonms06277\\d$\\uat\\rbsfm\\cp\\ceemea-2009WK52-22");
      project.addProperty("include.servers.regex", "ceemea-pricing-instmaint" );
      project.addProperty("exclude.servers.regex", "" ); 
      project.addResource("cpbuild/config/rates/all.properties","rbsfm",190762);
      project.addResource("cpbuild/config/templates/pricing.xml","rbsfm",189320);
      project.addResource("cpbuild/config/includes/instmaint/InstrumentMaintenance.xml","rbsfm",191239);    
      project.addResource("cpbuild/config/includes/instmaint/ceemeaCoreInstrument.xml","rbsfm",191237);
      project.addResource("cpbuild/config/includes/instmaint/ceemeaEnrichers.xml","rbsfm",191240); 
      project.addResource("egpricing/src/com/rbsfm/fi/instmaint/enrich/ObligationTypeEnricher.java","rbsfm",191247);
      project.addResource("egpricing/src/com/rbsfm/fi/pricing/dependency/maintenance/RequestBondsConsumer.java","rbsfm",191244);
      project.addResource("ficommon/src/com/rbsfm/fi/pricing/external/attribute/AttributeNames.java","rbsfm",191249);
      persister.write(project, System.out);
   }
}
