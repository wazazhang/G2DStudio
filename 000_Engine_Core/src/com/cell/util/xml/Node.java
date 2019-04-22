package com.cell.util.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.cell.CUtil;


public class Node 
{
	private String _name;
	private String _content = "";
	private Map<String, String> _attributes = new LinkedHashMap<String, String>();
	private List<Node> _childs = new ArrayList<Node>();
	
	public String name() {
		return _name;
	}
	
	public String content() {
		return _content;
	}
	
	public Map<String, String> attributes()
	{
		return new LinkedHashMap<String, String>(_attributes);
	}
	
	public String attribute(String key)
	{
		return _attributes.get(key);
	}
	
	public Node child(String key)
	{
		for (Node c : _childs) {
			if (c.name().equals(key)) {
				return c;
			}
		}
		return null;
	}	
	
	public List<Node> childs(String key)
	{
		return new ArrayList<Node>(_childs);
	}
	

	private String toXmlString(int deep)
	{
		StringBuilder attr = new StringBuilder();
		for (Map.Entry<String, String> e : _attributes.entrySet()) {
			attr.append(e.getKey()+"=\""+e.getValue()+"\" ");
		}
		
		String prefix = CUtil.stringFill("  ", deep);
		
		StringBuilder sb = new StringBuilder();
		
		if (_childs.isEmpty() && _content.isEmpty())
		{
			if (attr.length() > 0) {
				sb.append(prefix + "<" + _name + " " + attr + "/>");
			} else {
				sb.append(prefix + "<" + _name + "/>");
			}
		} 
		else 
		{
			String begin = "";
			if (attr.length() > 0) {
				begin = ("<" + _name + " " + attr + ">");
			} else {
				begin = ("<" + _name + ">");
			}
			String end = ("</" + _name + ">");;

			if (_childs.isEmpty()) 
			{
				sb.append(prefix + begin + _content + end);
			}
			else
			{
				sb.append(prefix + begin + "\n");
				for (Node c : _childs) {
					String ss = c.toXmlString(deep + 1);
					sb.append(ss + "\n");
				}
				sb.append(prefix + end);
			}
		
		}
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return toXmlString(0);
	}
	
//	-------------------------------------------------------------------------------
	
	public static Node createFrom(InputStream xmlFile) throws Exception
	{
		DocumentBuilderFactory domfac = DocumentBuilderFactory.newInstance();
		DocumentBuilder dombuilder = domfac.newDocumentBuilder();
		org.w3c.dom.Document doc = dombuilder.parse(xmlFile);
		doc.normalizeDocument();
		Node root = new Node();
		initNode(root, doc.getDocumentElement());
		return root;
	}
	
	public static Node parseFrom(String xml) throws Exception
	{
		InputStream input = new ByteArrayInputStream(xml.getBytes("UTF-8"));
		return createFrom(input);
	}
	
	private static void initNode(Node node, org.w3c.dom.Element xmlnode)
	{
		node._name = xmlnode.getTagName();
//		node._content = xmlnode.getTextContent();
		for (int i=0; i<xmlnode.getAttributes().getLength(); i++) {
			org.w3c.dom.Attr akey = (org.w3c.dom.Attr)xmlnode.getAttributes().item(i);
			node._attributes.put(akey.getName(), akey.getValue());
		}
		for (int i=0; i<xmlnode.getChildNodes().getLength(); i++) {
			org.w3c.dom.Node achild = xmlnode.getChildNodes().item(i);
			if (achild instanceof org.w3c.dom.Element) {
				Node child = new Node();
				initNode(child, (org.w3c.dom.Element)achild);
				node._childs.add(child);
			}
			if (achild instanceof org.w3c.dom.Text) {
				node._content += ((org.w3c.dom.Text)achild).getData().trim();
			}
		}
	}
	
}
