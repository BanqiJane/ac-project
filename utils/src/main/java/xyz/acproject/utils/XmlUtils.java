package xyz.acproject.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import xyz.acproject.utils.http.OkHttp3Utils;
import xyz.acproject.utils.io.ByteUtils;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Jane
 * @ClassName XmlUtils
 * @Description TODO
 * @date 2022/4/24 17:01
 * @Copyright:2022
 */
public class XmlUtils {

    private volatile  static XmlUtils xmlUtils;

    private  SAXBuilder builder;


    private XmlUtils(){
        builder = new SAXBuilder();
    }

    public static XmlUtils getXmlUtils() {

        if(xmlUtils==null) {
            synchronized (XmlUtils.class) {
                if(xmlUtils==null) {
                    xmlUtils = new XmlUtils();
                }
            }
        }
        return xmlUtils;
    }

    public  static <T> T getData(String xml, Class<T> clazz) {
        if(null==xml||"".equals(xml)){
            return null;
        }
        Pattern p = Pattern.compile(".*<!\\[CDATA\\[(.*)\\]\\]>.*");
        Matcher m = p.matcher(xml);
        if(m.matches()) {
            return clazz.cast(m.group(1));
        }
        return null;
    }

    public static Object getData(String xml,String key) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc  = builder.build(ByteUtils.parse_inputStream(xml));
        Element rootElement = doc.getRootElement();
        Map<String, Object> mapXml = new TreeMap<String, Object>();
        element2Map(mapXml, rootElement);
        return mapXml.get(key);
    }


    public static String xml2Json(String responseXmlTemp, JSONObject node, String currentNode, String listcurrentNode) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(ByteUtils.parse_inputStream(responseXmlTemp));
        Element rootElement = doc.getRootElement();
        Map<String, Object> mapXml = new TreeMap<String, Object>();
        element2Map(mapXml, rootElement, node, currentNode, listcurrentNode);
        String jsonXml = FastJsonUtils.toJson(mapXml);
        return jsonXml;
    }

    /**
     * ???xml???????????????????????????Json??????
     *
     * @param responseXmlTemp
     * @return
     * @throws IOException
     * @throws JDOMException
     */
    public static String xml2Json(String responseXmlTemp) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc  = builder.build(ByteUtils.parse_inputStream(responseXmlTemp));
        Element rootElement = doc.getRootElement();
        Map<String, Object> mapXml = new TreeMap<String, Object>();

        element2Map(mapXml, rootElement);
        String jsonXml = FastJsonUtils.toJson(mapXml);
        return jsonXml;
    }


    public static Map<String, Object> xml2map(String responseXmlTemp, JSONObject node, String currentNode,
                                              String listCurrentNode) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc  = builder.build(ByteUtils.parse_inputStream(responseXmlTemp));
        Element rootElement = doc.getRootElement();
        Map<String, Object> mapXml = new TreeMap<String, Object>();
        element2Map(mapXml, rootElement, node, currentNode, listCurrentNode);
        return mapXml;
    }

    public static Map<String, Object> xml2map(String responseXmlTemp) throws Exception {
        SAXBuilder builder = new SAXBuilder();
        Document doc  = builder.build(ByteUtils.parse_inputStream(responseXmlTemp));
        Element rootElement = doc.getRootElement();
        Map<String, Object> mapXml = new TreeMap<String, Object>();
        element2Map(mapXml, rootElement);
        return mapXml;
    }

    /**
     * ??????????????????????????????xml??????map
     *
     * @param map
     * @param rootElement
     */
    @SuppressWarnings( {"unchecked", "rawtypes"})
    public static void element2Map(Map<String, Object> map, Element rootElement, JSONObject node, String currentNode,
                                   String listcurrentNode) {
        // ??????????????????????????????
        List<Element> elements = rootElement.getChildren();
        int elementsSize = elements.size();
        if (elementsSize == 0) {
            // ???????????????????????????????????????????????????????????????
            if (node.getJSONObject(currentNode) != null) {
                if (node.getJSONObject(currentNode).keySet().toString().contains(rootElement.getName())) {
                    map.put(rootElement.getName(), rootElement.getText());
                }
            }
        } else if (elementsSize == 1 && !elements.get(0).getName().equals(listcurrentNode) ) {
            // ???????????????????????????????????????list????????????????????????
            Map<String, Object> tempMap = new TreeMap<String, Object>();
            if (node.getJSONObject(currentNode) != null) {
                element2Map(tempMap, elements.get(0), node.getJSONObject(currentNode), elements.get(0).getName(),
                        listcurrentNode);
                if (node.getJSONObject(currentNode).keySet().contains(elements.get(0).getName())) {
                    map.put(elements.get(0).getName(), tempMap);
                }
            }

        } else {
            // ?????????????????????????????????list?????????????????????????????????????????????????????????????????????
            Map<String, Object> tempMap = new TreeMap<String, Object>();
            for (Element element : elements) {
                tempMap.put(element.getName(), null);
            }
            Set<String> keySet = tempMap.keySet();
            for (String string : keySet) {
                Namespace namespace = elements.get(0).getNamespace();
                List<Element> sameElements = rootElement.getChildren(string);
                // ???????????????????????????1??????????????????list
                int sameElementSize = sameElements.size();
                if (sameElementSize > 1) {
                    List<Map> list = new ArrayList<Map>();
                    for (Element element : sameElements) {
                        Map<String, Object> sameTempMap = new TreeMap<String, Object>();
                        if (node.getJSONObject(currentNode).keySet().toString().contains(string)) {
                            if (node.getJSONObject(currentNode) != null) {
                                element2Map(sameTempMap, element, node.getJSONObject(currentNode), string,
                                        listcurrentNode);
                                list.add(sameTempMap);
                            }
                        }
                    }

                    if (node.getJSONObject(currentNode).keySet().toString().contains(string)) {
                        if (node.getJSONObject(currentNode) != null) {
                            map.put(string, list);
                        }
                    }
                } else {
                    // ????????????????????????1????????????
                    if (listcurrentNode != null) {
                        String[] strarr = listcurrentNode.split(",");
                        for (int i = 0; i < strarr.length; i++) {
                            if (strarr[i].equals(string)) {
                                List<Map> list = new ArrayList<Map>();
                                for (Element element : sameElements) {
                                    Map<String, Object> sameTempMap = new TreeMap<String, Object>();
                                    if (node.getJSONObject(currentNode).keySet().toString().contains(string)) {
                                        if (node.getJSONObject(currentNode) != null) {
                                            element2Map(sameTempMap, element, node.getJSONObject(currentNode), string,
                                                    listcurrentNode);
                                            list.add(sameTempMap);
                                        }
                                    }
                                }
                                if (node.getJSONObject(currentNode).keySet().toString().contains(string)) {
                                    if (node.getJSONObject(currentNode) != null) {
                                        map.put(string, list);
                                    }
                                }
                            } else {
                                Map<String, Object> sameTempMap = new TreeMap<String, Object>();
                                if (node.getJSONObject(currentNode).keySet().toString().contains(string)) {
                                    if (node.getJSONObject(currentNode) != null) {
                                        element2Map(sameTempMap, sameElements.get(0), node.getJSONObject(currentNode),
                                                string, listcurrentNode);
                                        map.put(string, sameTempMap);
                                    }
                                }
                            }
                        }
                    } else {
                        Map<String, Object> sameTempMap = new TreeMap<String, Object>();
                        if (node.getJSONObject(currentNode).keySet().toString().contains(string)) {
                            if (node.getJSONObject(currentNode) != null) {
                                element2Map(sameTempMap, sameElements.get(0), node.getJSONObject(currentNode), string,
                                        listcurrentNode);
                                map.put(string, sameTempMap);
                            }
                        }
                    }
                }

            }
        }
    }

    public static void element2Map(Map<String, Object> map, Element rootElement) {
        // ??????????????????????????????
        List<Element> elements = rootElement.getChildren();
        int elementsSize = elements.size();
        if (elementsSize == 0) {
            // ???????????????????????????????????????????????????????????????
            // if(node.getJSONObject(currentNode).keySet().toString().contains(rootElement.getName())) {
            map.put(rootElement.getName(), rootElement.getText());
            // }
        } else if (elementsSize == 1) {
            // ???????????????????????????????????????list????????????????????????
            Map<String, Object> tempMap = new TreeMap<String, Object>();
            element2Map(tempMap, elements.get(0));
            // if(node.getJSONObject(currentNode).keySet().contains(elements.get(0).getName())) {
            map.put(elements.get(0).getName(), tempMap);
            // }
        } else {
            // ?????????????????????????????????list?????????????????????????????????????????????????????????????????????
            Map<String, Object> tempMap = new TreeMap<String, Object>();
            for (Element element : elements) {
                tempMap.put(element.getName(), null);

            }
            Set<String> keySet = tempMap.keySet();
            for (String string : keySet) {
                Namespace namespace = elements.get(0).getNamespace();
                List<Element> sameElements = rootElement.getChildren(string);
                // ???????????????????????????1??????????????????list
                int sameElementSize = sameElements.size();
                if (sameElementSize > 1) {
                    List<Map> list = new ArrayList<Map>();
                    for (Element element : sameElements) {
                        Map<String, Object> sameTempMap = new TreeMap<String, Object>();
                        element2Map(sameTempMap, element);
                        list.add(sameTempMap);
                    }
                    map.put(string, list);
                } else {
                    // ????????????????????????1????????????
                    Map<String, Object> sameTempMap = new TreeMap<String, Object>();
                    element2Map(sameTempMap, sameElements.get(0));
                    map.put(string, sameTempMap);
                }
            }
        }

    }

    @SuppressWarnings("unchecked")
    public static void parseData(Map<?, ?> map,JSONObject jsonObject) {
        if (map.size() == 1) {
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if (value.toString().startsWith("[") && value.toString().endsWith("]")) {
                    jsonObject.put(String.valueOf(key), value);
                    // List<?> list = (List<?>)value;
                    // Iterator<?> iterator = list.iterator();
                    // while(iterator.hasNext()){
                    // value = iterator.next();
                    // parseData((Map<String, Object>)value);
                    // }
                    // System.out.println(key+"  "+value);
                } else if (!value.toString().startsWith("{") && !value.toString().endsWith("}")) {
                    if (jsonObject.get(key) != null) {
                        if (!jsonObject.get(key).toString().startsWith("[")
                                && !jsonObject.get(key).toString().endsWith("]")) {
                            jsonObject.put(String.valueOf(key), "" + jsonObject.getString((String)key).toString() + "|"
                                    + (value).toString() + "");
                        } else {
                            jsonObject.put(String.valueOf(key), "\"" + jsonObject.getJSONArray((String)key).toString() + "|"
                                    + FastJsonUtils.toJson(value).toString() + "\"");
                        }
                    } else {
                        jsonObject.put(String.valueOf(key), value);
                    }
                } else {
                    parseData((Map<String, Object>)value,jsonObject);
                }
            }
        } else {
            for (Object key : map.keySet()) {
                Object value = map.get(key);
                if (value.toString().startsWith("[") && value.toString().endsWith("]")) {

                    // List<?> list = (List<?>)value;
                    // for(int i = 0;i < list.size();i++)
                    // {
                    // Map<String,Object> map1 = (Map<String, Object>) list.get(i);
                    // parseData(map1);
                    // }
                    if (jsonObject.get(key) != null) {
                        jsonObject.put(String.valueOf(key), "\"" + jsonObject.getJSONArray((String)key).toString() + "|"
                                +  FastJsonUtils.toJson(value).toString() + "\"");
                    } else {
                        jsonObject.put(String.valueOf(key), value);
                    }
                } else {
                    if (value.toString().startsWith("{") && value.toString().endsWith("}")) {
                        parseData((Map<String, Object>)value,jsonObject);
                    }
                }
            }
        }
    }
}
