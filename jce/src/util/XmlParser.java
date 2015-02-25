package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


/**
 * 
* @����: XmlParser 
* @�๦������: ����xml
* @ʱ�� ��2011-4-13 ����08:33:50
 */
public class XmlParser{
	/**
	 * 
	 * @�����������������ɿյ�xml�ļ�ͷ
	 * @������:createEmptyXmlFile
	 * @param xmlPath
	 * @�������ͣ�Document
	 * @ʱ�䣺2011-4-14����12:44:20
	 */
	public static Document createEmptyXmlFile(String xmlPath){
		if(xmlPath==null || xmlPath.equals(""))
			return null;

		XMLWriter output;
		Document document = DocumentHelper.createDocument();
		    
		OutputFormat format = OutputFormat.createPrettyPrint();
		try {
			output = new XMLWriter(new FileWriter(xmlPath), format);
			output.write(document);
			output.close();
		} catch (IOException e) {
			return null;
		}
		return document;
	}
	/**
	 * ����xml�ļ�·��ȡ��document����
	 * @param xmlPath
	 * @return
	 * @throws DocumentException
	 */
	public static Document getDocument(String xmlPath){
		if(xmlPath==null || xmlPath.equals(""))
			return null;

		File file = new File(xmlPath);
		if(file.exists()==false){
			return createEmptyXmlFile(xmlPath);
		}
		
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(xmlPath);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return document;
	}
	/**
	 * 
	 * @���������������õ����ڵ�
	 * @������:getRootEleme
	 * @param DOC����
	 * @�������ͣ�Element
	 * @ʱ�䣺2011-4-8����12:54:02
	 */
	public static Element getRootNode(Document document){
		if(document==null)
			return null;
		
		Element root = document.getRootElement();
		return root;
	}
	/**
	 * 
	 * @������������: ����·��ֱ���õ����ڵ�
	 * @������:getRootElement
	 * @param xmlPath
	 * @return
	 * @throws DocumentException @�������� :
	 * @�������ͣ�Element
	 * @ʱ�䣺2011-4-14����03:01:14
	 */
	public static Element getRootNode(String xmlPath) {
			if(xmlPath==null||(xmlPath.trim()).equals(""))
				return null;
			Document document = getDocument(xmlPath);
			if(document==null)
				return null;
			return getRootNode(document);
	   }
	/**
	 * 
	 * @������������:�õ�ָ��Ԫ�صĵ�����
	 * @������:getIterator
	 * @param parent
	 * @�������ͣ�Iterator<Element>
	 * @ʱ�䣺2011-4-14����11:29:18
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<Element> getIterator(Element parent){
		if(parent == null)
			return null;
		Iterator<Element> iterator = parent.elementIterator();
		return iterator;
	}
	
	/**
	 * 
	 * @������������: �����ӽڵ����Ƶõ�ָ�����ӽڵ�
	 * @������:getChildElement
	 * @param parent
	 * @param childName
	 * @�������ͣ�Element
	 * @ʱ�䣺2011-4-14����11:18:03
	 */
	@SuppressWarnings("unchecked")
	public static  List<Element> getChildElements(Element parent,String childName){
		childName  = childName.trim();
		if (parent==null) 
			return null;
		childName += "//";
		List<Element> childElements = parent.selectNodes(childName);
		return childElements;
	}

	/**
	 * 
	 * @��������������TODO
	 * @������:getChildList
	 * @param node
	 * @return @�������� :
	 * @�������ͣ�List<Element>
	 * @ʱ�䣺2011-4-14����12:21:52
	 */
	public static  List<Element> getChildList(Element node){
		if (node==null) 
			return null;	
		Iterator<Element> itr = getIterator(node);
		if(itr==null)
			return null;
		List<Element> childList = new ArrayList<Element>();
		while(itr.hasNext()){
			Element kidElement = itr.next();
			if(kidElement!=null){
				childList.add(kidElement);
			}
		}
		return childList;
	}
	/**
	 * 
	 * @������������ : ��ѯû���ӽڵ�Ľڵ㣬ʹ��xpath��ʽ
	 * @������:getSingleNode
	 * @param parent
	 * @param nodeNodeName
	 * @return @�������� : ���ڵ㣬�ӽڵ�����
	 * @�������ͣ�Node
	 * @ʱ�䣺2011-4-14����12:38:25
	 */
	public static Node getSingleNode(Element parent,String nodeNodeName){
		nodeNodeName = nodeNodeName.trim();
		String xpath = "//";
		if(parent==null)
			return null;
		if (nodeNodeName==null||nodeNodeName.equals("")) 
			return null;
		xpath += nodeNodeName;
		Node kid = parent.selectSingleNode(xpath);
		return kid;
	}
	/**
	 * 
	 * @���������������õ��ӽڵ㣬��ʹ��xpath
	 * @������:getChild
	 * @param parent
	 * @param childName
	 * @return @�������� :
	 * @�������ͣ�Element
	 * @ʱ�䣺2011-4-14����12:53:22
	 */
	@SuppressWarnings("rawtypes")
	public static Element getChild(Element parent,String childName){
		childName = childName.trim();
		if(parent==null)
			return null;
		if(childName==null || childName.equals(""))
			return null;
		Element e = null;
		Iterator it = getIterator(parent);
		while(it!=null && it.hasNext()){
			Element k = (Element)it.next();
			if(k==null)continue;
			if(k.getName().equalsIgnoreCase(childName)){
				e = k;
				break;
			}
		}
		return e;
	}
	/**
	 * 
	 * @���������������жϽڵ��Ƿ����ӽڵ�
	 * @������:hasChild
	 * @param e
	 * @�������ͣ�boolean
	 * @ʱ�䣺2011-4-14����01:43:48
	 */
	public static boolean hasChild(Element e){
		if(e==null)
			return false;
		return e.hasContent();
	}
	/**
	 * 
	 * @���������������õ�ָ���ڵ�����Եĵ�����
	 * @������:getAttrIterator
	 * @param e
	 * @�������ͣ�Iterator<Attribute>
	 * @ʱ�䣺2011-4-14����01:42:38
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<Attribute> getAttrIterator(Element e){
		if(e==null)
			return null;
		Iterator<Attribute> attrIterator = e.attributeIterator();
		return attrIterator;
	}
	/**
	 * 
	 * @������������������ָ���ڵ����������
	 * @������:getAttributeList
	 * @param e
	 * @return �ڵ����Ե�list����
	 * @�������ͣ�List<Attribute>
	 * @ʱ�䣺2011-4-14����01:41:38
	 */
	public static List<Attribute> getAttributeList(Element e){
		if(e==null)
			return null;
		List<Attribute> attributeList = new ArrayList<Attribute>();
		Iterator<Attribute> atrIterator = getAttrIterator(e);
		if(atrIterator == null)
			return null;
		while (atrIterator.hasNext()) {
			Attribute attribute = atrIterator.next();
			attributeList.add(attribute);
		}
		return attributeList;
	}
	/**
	 * 
	 * @��������������  �õ�ָ���ڵ��ָ������
	 * @������:getAttribute
	 * @param element ָ����Ԫ��
	 * @param attrName ��������
	 * @return Attribute
	 * @�������ͣ�Attribute
	 * @ʱ�䣺2011-4-14����01:45:27
	 */
	public static Attribute getAttribute(Element element , String attrName){
		attrName = attrName.trim();
		if(element==null)
			return null;
		if(attrName==null||attrName.equals(""))
			return null;
		Attribute attribute = element.attribute(attrName);
		return attribute;
	}
	/**
	 * 
	 * @������������:��ȡָ���ڵ�ָ�����Ե�ֵ
	 * @������:attrValue
	 * @param e
	 * @param attrName
	 * @�������ͣ�String
	 * @ʱ�䣺2011-4-14����02:36:48
	 */
	public static String attrValue(Element e,String attrName){
		attrName = attrName.trim();
		if(e == null)
			return null;
		if (attrName== null || attrName.equals(""))
			return null;
		return e.attributeValue(attrName);
	}
	
	/**
	 * 
	 * @���������������õ�ָ���ڵ���������Լ�����ֵ
	 * @������:getNodeAttrMap
	 * @return ���Լ���
	 * @�������ͣ�Map<String,String>
	 * @ʱ�䣺2011-4-15����10:00:26
	 */
	public static Map<String,String> getNodeAttrMap(Element e){
		Map<String,String> attrMap = new HashMap<String, String>();
		if (e == null) {
			return null;
		}
		List<Attribute> attributes = getAttributeList(e);
		if (attributes == null) {
			return null;
		}
		for (Attribute attribute:attributes) {
			String attrValueString = attrValue(e, attribute.getName());
			attrMap.put(attribute.getName(), attrValueString);
		}
		return attrMap;
	}
	/**
	 * 
	 * @������������: ����ָ���ڵ����û���ӽڵ��Ԫ�ص�textֵ
	 * @������:getSingleNodeText
	 * @param e
	 * @return @�������� :
	 * @�������ͣ�Map<String,String>
	 * @ʱ�䣺2011-4-15����12:24:38
	 */
	public static Map<String, String> getSingleNodeText(Element e){
		Map<String, String> map = new HashMap<String, String>();
		if(e == null)
			return null;
		List<Element> kids = getChildList(e);
		for(Element e2 :kids){
			if(e2.getTextTrim()!=null){
				map.put(e2.getName(), e2.getTextTrim());
			}
		}
		return map;
	}
	
	/**
	 * 
	 * @���������������������ڵ��£�û���ӽڵ��Ԫ�ؽڵ㣬�����˽ڵ��textֵ����map�з���
	 * @������:getSingleNodeText
	 * @param xmlFilePath
	 * @return @�������� :
	 * @�������ͣ�Map<String,String>
	 * @ʱ�䣺2011-4-15����12:23:30
	 */
	public static Map<String,String> getSingleNodeText(String xmlFilePath){
		xmlFilePath = xmlFilePath.trim();
		if(xmlFilePath==null||xmlFilePath.equals("")){
			return null;
		}
		Element rootElement = getRootNode(xmlFilePath);
		if(rootElement==null||!hasChild(rootElement)){
			return null;
		}
		return getSingleNodeText(rootElement);
	}
	/**
	 * 
	 * @������������:����xml·����ָ���Ľڵ�����ƣ��õ�ָ���ڵ�,�Ӹ��ڵ㿪ʼ��
	 * @������:getNameNode
	 * @param xmlFilePath
	 * @param tagName
	 * @param flag : ָ��Ԫ�صĸ���
	 * @�������ͣ�Element ָ���Ľڵ�
	 * @ʱ�䣺2011-4-15����12:22:35
	 * 
	 */
	
	public enum Flag{one,more}
	@SuppressWarnings("unchecked")
	public static <T>T getNameNode(String xmlFilePath,String tagName,Flag flag){
		xmlFilePath = xmlFilePath.trim();
		tagName = tagName.trim();
		if(xmlFilePath==null||tagName==null||xmlFilePath.equals("")||tagName.equals(""))
			return null;
		Element rootElement = getRootNode(xmlFilePath);
		if(rootElement==null)
			return null;
		List<Element> tagElementList = getNameElement(rootElement, tagName);
		if(tagElementList == null)
			return null;
		switch (flag) {
		case one:
			return (T) tagElementList.get(0);
		}
		return (T) tagElementList;
	}
	/**
	 * 
	 * @������������:�õ�ָ���ڵ��������ӽڵ�����Լ���
	 * @������:getNameNodeAllAttributeMap
	 * @param e
	 * @return @�������� :
	 * @�������ͣ�Map<Integer,Object>
	 * @ʱ�䣺2011-4-18����04:40:14
	 */
	public static Map<Integer,Object> getNameNodeAllKidsAttributeMap(Element parent){
		Map<Integer,Object> allAttrMap = new HashMap<Integer, Object>();
		if(parent == null)
			return null;
		List<Element> childlElements = getChildList(parent);
		if (childlElements == null)
			return null; 
		for (int i = 0; i < childlElements.size(); i++) {
			Element childElement = childlElements.get(i);
			Map<String,String> attrMap = getNodeAttrMap(childElement);
			allAttrMap.put(i,attrMap);
		}
		return allAttrMap;
	}
	/**
	 * 
	 * @������������:����xml�ļ���·����ָ���Ľڵ����Ƶõ�ָ���ڵ������ӽڵ���������Լ���
	 * @������:getNameNodeAllAttributeMap
	 * @param xmlFileName
	 * @param nodeName
	 * @return @�������� :
	 * @�������ͣ�Map<Integer,Object>
	 * @ʱ�䣺2011-4-18����04:51:46
	 */
	@SuppressWarnings("unchecked")
	public static <T>T getNameNodeAllAttributeMap(String xmlFilePath,String nodeName,Flag flag){
		nodeName = nodeName.trim();
		Map<String, String> allAttrMap = null;
		Map<Integer,Map<String,String>> mostKidsAllAttriMap = new HashMap<Integer, Map<String,String>>();
		if (xmlFilePath==null||nodeName==null||xmlFilePath.equals("")||nodeName.equals(""))
			return null;
		switch (flag) {
		case one:
			Element nameNode = getNameNode(xmlFilePath, nodeName,Flag.one);
			allAttrMap = getNodeAttrMap(nameNode);
			return (T) allAttrMap;
		case more:
			List<Element> nameKidsElements = getNameNode(xmlFilePath, nodeName, Flag.more);
			for (int i = 0; i < nameKidsElements.size(); i++) {
				Element kid = nameKidsElements.get(i);
				allAttrMap = getNodeAttrMap(kid);
				mostKidsAllAttriMap.put(i,allAttrMap);
			}
			return (T) mostKidsAllAttriMap;
		}
		return null;
	}
	/**
	 * 
	 * @������������:����ָ���Ľڵ������еĽڵ�
	 * @������:ransack
	 * @param element @�������� :
	 * @�������ͣ�void
	 * @ʱ�䣺2011-4-18����05:25:41
	 */
	public static List<Element> ransack(Element element,List<Element> allkidsList){
		if(element == null)
			return null;
		if(hasChild(element)){
			List<Element> kids = getChildList(element);
			for (Element e : kids) {
				allkidsList.add(e);
				ransack(e,allkidsList);
			}
		}
		return allkidsList;
	}
	/**
	 * 
	 * @������������:�õ�ָ���ڵ��µ�ָ���ڵ㼯��
	 * @������:getNameElement
	 * @param element
	 * @param nodeName
	 * @return @�������� :
	 * @�������ͣ�Element
	 * @ʱ�䣺2011-4-18����06:18:56
	 */
	public static List<Element> getNameElement(Element element ,String nodeName){
		nodeName = nodeName.trim();
		List<Element> kidsElements = new ArrayList<Element>();
		if(element == null)
			return null;
		if(nodeName == null || nodeName.equals(""))
			return null;
		List<Element> allKids = ransack(element,new ArrayList<Element>());
		if(allKids == null)
			return null;
		for (int i = 0; i < allKids.size(); i++) {
			Element kid = allKids.get(i);
			if(nodeName.equals(kid.getName()))
				kidsElements.add(kid);
		}
		return kidsElements;
	}
	
	/**
	 * 
	 * @������������:��֤�ڵ��Ƿ�Ψһ
	 * @������:validateSingle
	 * @param element
	 * @�������ͣ�int �ڵ�Ψһ����1,�ڵ㲻Ψһ���ش���һ����������
	 * @ʱ�䣺2011-4-20����04:36:22
	 */
	public static int validateSingle(Element element){
		int j = 1;
		if(element == null)
			return j;
		Element parent = element.getParent();
		List<Element> kids = getChildList(parent);
		for (Element kid : kids) {
			if(element.equals(kid))
				j++;
		}
		return j;
	}
	
	public static void main(String[] args){
		String xml = "D:/user.xml";
//		Document doc = getDocument(xml);
//		Element  root = getRootNode(doc);
		Element  root =getRootNode(xml);
		
		List<Element> users = getNameElement(root, "user");
		String age = getAttribute(users.get(0), "age").getText();
		System.out.println(age);
	}
}