package xml;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang3.StringUtils;

import structure.Node;

public class XmlBuilder {
	
	private static final String ATTRIBUTE_NODE_MARKER = "=";
	private static final String TEXT_NODE_MARKER = "_!_";

	private Node<String> _tree;
	
	public static final String ATTRIBUTE_REG_EXP = ".+" + ATTRIBUTE_NODE_MARKER + "'.+'";
	
	public XmlBuilder(String root) {
		_tree = new Node<String>(root);
	}
	
	public XmlBuilder withTag(String tagName){
		_tree = _tree.add(tagName);
		return this;
	}
	
	public XmlBuilder withAttribute(String name, String value){
		_tree.addChild(name + ATTRIBUTE_NODE_MARKER + "'" + value + "'");
		return this;
	}
	
	public XmlBuilder withText(String text){
		_tree.addChild(TEXT_NODE_MARKER + text);
		return this;
	}
	
	public XmlBuilder withTag(String tagName, String text){
		withTag(tagName);
		withText(text);
		_tree = _tree.parent();
		return this;
	}
	
	public XmlBuilder parent() {
		_tree = _tree.parent();
		return this;
	}
	
	public XmlBuilder toTag(String nodeName){
		while(!_tree.data().equals(nodeName)){
			parent();
		}
		return this;
	}
	
	public String build(){
		return print(_tree.root());
	}
	
	public String buildFormatted(){
		try {
			StreamResult xmlOutput = new StreamResult(new StringWriter());
			
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			transformer.transform(new StreamSource(new StringReader(build())), xmlOutput);
			
			return xmlOutput.getWriter().toString();
		} catch (TransformerException e) {
			throw new RuntimeException(build(), e);
		} 
	}
	
	private String print(Node<String> node) {
		
		if(node.isLeaf()){
			if(node.data().contains(TEXT_NODE_MARKER)){
				return node.data().replace(TEXT_NODE_MARKER, "");
			}
			if(node.data().contains(ATTRIBUTE_NODE_MARKER)){
				return "";
			}
			
			return "<" + node + "/>"; 
		} else {
			String value = "<" + node + attributesOf(node) + ">";
			for (Node<String> child : node.children()) {
				value += print(child); 
			}
			value += "</" + node + ">";
			
			return value;
		}
	}

	private String attributesOf(Node<String> node) {
		List<String> attributes = new ArrayList<String>();
		for (Node<String> child: node.children()) {
			if(child.data().matches(ATTRIBUTE_REG_EXP)){
				attributes.add(child.data());
			}
		}
		
		return attributes.isEmpty() ? "" : " " + StringUtils.join(attributes, " ");
	}
}
