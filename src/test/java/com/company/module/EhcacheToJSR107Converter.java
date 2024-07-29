package com.company.module;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EhcacheToJSR107Converter {
    public static void main(String[] args) {
        try {
            // Load the Ehcache XML configuration file
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document ehcacheDoc = builder.parse(new File("/SOURCE_CODE/service-base/src/main/resources/ehcache.xml"));

            // Create a new document for the JSR-107 configuration
            Document jsr107Doc = builder.newDocument();

            // Create the root element and set the namespace
            Element configElement = jsr107Doc.createElement("config");
            configElement.setAttribute("xmlns", "http://www.ehcache.org/v3");
            configElement.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            configElement.setAttribute("xsi:schemaLocation", "http://www.ehcache.org/v3 http://www.ehcache.org/schema/ehcache-core.xsd");

            jsr107Doc.appendChild(configElement);

            // Define a map for common configuration properties (e.g., key and value types)
            Map<String, String> commonConfig = new HashMap<>();
//            commonConfig.put("key-type", "java.lang.String");
//            commonConfig.put("value-type", "java.lang.String");

            // Get all <cache> elements from the Ehcache document
            NodeList cacheElements = ehcacheDoc.getElementsByTagName("cache");
            for (int i = 0; i < cacheElements.getLength(); i++) {
                Element cacheElement = (Element) cacheElements.item(i);

                // Create a new <cache> element in the JSR-107 configuration
                Element jsr107CacheElement = jsr107Doc.createElement("cache");

                // Set the cache alias from the Ehcache configuration
                jsr107CacheElement.setAttribute("alias", cacheElement.getAttribute("name"));

                // Copy common configuration properties
                for (Map.Entry<String, String> entry : commonConfig.entrySet()) {
                    Element commonPropertyElement = jsr107Doc.createElement(entry.getKey());
                    commonPropertyElement.setTextContent(entry.getValue());
                    jsr107CacheElement.appendChild(commonPropertyElement);
                }

                // Extract cache-specific properties and copy them to the JSR-107 configuration
                // maxElementsInMemory:   <heap unit="entries">200</heap>
//                String maxElementsInMemory = cacheElement.getAttribute("maxElementsInMemory");
//                addElementProperties(jsr107Doc, jsr107CacheElement, "unit","entries","heap", maxElementsInMemory);

                // timeToLiveSeconds
                String timeToLiveSeconds = cacheElement.getAttribute("timeToLiveSeconds");
                addElementNested(jsr107Doc, jsr107CacheElement, "expiry","ttl",timeToLiveSeconds);

                // offheap
                addElementNestedProperties(jsr107Doc, jsr107CacheElement, "resources","offheap","unit","MB", "2");

                //   ??
                //String overflowToDisk = cacheElement.getAttribute("overflowToDisk");



//                <expiry>
//                    <ttl >800</ttl>
//                </expiry>
//                <resources>
//                    <offheap unit="MB">100</offheap>
//                </resources>

                configElement.appendChild(jsr107CacheElement);
            }

            // Save the generated JSR-107 XML configuration to a file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(jsr107Doc);
            StreamResult result = new StreamResult(new File("/SOURCE_CODE/service-base/src/main/resources/jsr107.xml"));
            transformer.transform(source, result);

            System.out.println("Conversion complete. JSR-107 XML configuration saved as jsr107.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addElementProperties(Document jsr107Doc, Element jsr107CacheElement,String pName, String pValue ,String name, String value){

        Element jsr107PropertyElementSub =  jsr107Doc.createElement(name);
        jsr107PropertyElementSub.setNodeValue(value);
        jsr107PropertyElementSub.setTextContent(value);
        jsr107PropertyElementSub.setAttribute(pName, pValue);
        jsr107CacheElement.appendChild(jsr107PropertyElementSub);
    }

    private static void addElement(Document jsr107Doc, Element jsr107CacheElement, String name, String value){

        Element jsr107PropertyElementSub =  jsr107Doc.createElement(name);
        jsr107PropertyElementSub.setNodeValue(value);
        jsr107PropertyElementSub.setTextContent(value);

        jsr107CacheElement.appendChild(jsr107PropertyElementSub);


    }

    private static void addElementNested(Document jsr107Doc, Element jsr107CacheElement, String name1, String name2, String value){

        Element jsr107PropertyElementSub1 =  jsr107Doc.createElement(name1);
        Element jsr107PropertyElementSub2 =  jsr107Doc.createElement(name2);
        jsr107PropertyElementSub2.setNodeValue(value);
        jsr107PropertyElementSub2.setTextContent(value);

        jsr107PropertyElementSub1.appendChild(jsr107PropertyElementSub2);
        jsr107CacheElement.appendChild(jsr107PropertyElementSub1);


    }

    private static void addElementNestedProperties(Document jsr107Doc, Element jsr107CacheElement, String name1, String name2,String propertyName,String propertyValue, String value){

        Element jsr107PropertyElementSub1 =  jsr107Doc.createElement(name1);
        Element jsr107PropertyElementSub2 =  jsr107Doc.createElement(name2);
        jsr107PropertyElementSub2.setNodeValue(value);
        jsr107PropertyElementSub2.setTextContent(value);
        jsr107PropertyElementSub2.setAttribute(propertyName, propertyValue);

        jsr107PropertyElementSub1.appendChild(jsr107PropertyElementSub2);
        jsr107CacheElement.appendChild(jsr107PropertyElementSub1);


    }
}

