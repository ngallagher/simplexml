package org.simpleframework.xml.stream;

import java.io.StringWriter;

import org.simpleframework.xml.ValidationTestCase;

public class MixedContentSerializationTest extends ValidationTestCase {

	public static final String EXPECTED = "<root>First line<a>A</a>Second line</root>";

	public void testMixedContent() throws Exception {
		StringWriter out = new StringWriter();
		OutputNode root = NodeBuilder.write(out, new Format(0))
			.getChild("root");

		OutputNode textNode1 = root.getChild(null);
		textNode1.setValue("First line");

		OutputNode a = root.getChild("a");
		a.setValue("A");

		OutputNode textNode2 = root.getChild(null);
		textNode2.setValue("Second line");

		root.commit();
		validate(out.toString());

		assertEquals(EXPECTED, out.toString());
	}

}
