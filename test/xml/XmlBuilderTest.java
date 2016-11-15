package xml;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;


public class XmlBuilderTest {
	
	@Test
	public void testAttributeMatch() {
		String entry = "c='111'";
		String otherEntry = "attribute:a='http://www.aaa.ch'";
		
		assertTrue(entry.matches(XmlBuilder.ATTRIBUTE_REG_EXP));
		assertTrue(otherEntry.matches(XmlBuilder.ATTRIBUTE_REG_EXP));
	}
	
	@Test
	public void leafNodeHasNotClosingTag() throws Exception {
		String xml = new XmlBuilder("div").withTag("br").build();
		
		assertThat(xml, is("<div><br/></div>"));
	}
	
	@Test
	public void rootNodeIsLeaf() throws Exception {
		String xml = new XmlBuilder("div").build();
		
		assertThat(xml, is("<div/>"));
	}
	
	@Test
	public void leafNodeWithAttributesHasNotClosingTag() throws Exception {
		String xml = new XmlBuilder("div").withAttribute("aaa", "bbb").build();
		
		assertThat(xml, is("<div aaa='bbb'/>"));
	}
	
	@Test
	public void nodeWithTextIsNotLeaf() throws Exception {
		String result = new XmlBuilder("div").withAttribute("aaa", "bbb").withText("xyz").build();
		
		assertThat(result, is("<div aaa='bbb'>xyz</div>"));
	}
	
	@Test
	public void testMultipleAttributes() {
		String expected = "<a:message attribute:a='http://www.aaa.ch' attribute:b='http://www.bbb.ch' c='111'>" +
							"<bb:cc attribute='xxx'/>"+
						 "</a:message>";
		
		String xml = new XmlBuilder("a:message").
							withAttribute("attribute:a", "http://www.aaa.ch").
							withAttribute("attribute:b", "http://www.bbb.ch").
							withAttribute("c", "111").
						withTag("bb:cc").
							withAttribute("attribute", "xxx").
					build();

		assertThat(xml, is(StringUtils.trim(expected)));
	}
	
	@Test
	public void testBuild() {
		String expected = 
				"<data>" +
					"<header>" +
						"<title style='.something #somewhere'>titleValue</title>" +
						"leafContent" +
						"<content>contentValue</content>"+
					"</header>" +
					"<body>" +
						"<div>" +
							"<span>value</span>" +
						"</div>" +
						"<div>some</div>" +
						"<div>" +
							"<element>otherValue</element>" +
							"<br class='.someClass'/>" +
							"<empty/>" +
						"</div>" +
					"</body>" +
					"<footer>endDocument</footer>" +
				"</data>";
		
		String xml = new XmlBuilder("data").
							withTag("header").
								withTag("title").
									withText("titleValue").
									withAttribute("style", ".something #somewhere").
								parent().
									withText("leafContent").
									withTag("content", "contentValue").
							parent().
								withTag("body").
									withTag("div").
										withTag("span", "value").
								parent().
									withTag("div", "some").
									withTag("div").
										withTag("element", "otherValue").
										withTag("br").
											withAttribute("class", ".someClass").
									parent().
										withTag("empty").
						toTag("data").
							withTag("footer", "endDocument").
					build();
		
		assertThat(xml, is(StringUtils.trim(expected)));					 
	}
	
	@Test
	public void testBuildFormatted() {
		String expected = "<data>\n" +
				          "  <a style=\".class\">text</a>\n" +
				          "</data>\n"; 
		
		String xml = new XmlBuilder("data").
						withTag("a").
							withAttribute("style", ".class").
							withText("text").
						buildFormatted();
	
		assertThat(xml, is(expected));
	}
}
