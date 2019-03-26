package org.simpleframework.xml.stream;

import java.io.StringWriter;

import org.simpleframework.xml.ValidationTestCase;

/**
 * Indentation can be disabled on individual nodes.
 */
public class DisableIndentationTest extends ValidationTestCase {
	public static final String EXPECTED = 
		"<root>\n"+
		"  <a><b>B</b></a>\n" +
		"</root>";

	public void testMixedContent() throws Exception {
		StringWriter out = new StringWriter();
		OutputNode root = NodeBuilder.write(out, new Format(2))
			.getChild("root");

		OutputNode a = root.getChild("a");
		a.setIndentationMode(IndentationMode.DISABLED);

		OutputNode b = a.getChild("b");
		b.setValue("B");
		
		root.commit();
		validate(out.toString());

		assertEquals(EXPECTED, out.toString());
	}
}
