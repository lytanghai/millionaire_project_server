package com.millionaire_project.millionaire_project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/source-of-news")
public class SourceOfNewsController {

    @GetMapping("/api/forex-factory")
    public List<Map<String, String>> getForexFactoryEvents() {
        List<Map<String, String>> eventsList = new ArrayList<>();
        try {
            String xmlUrl = "https://www.forexfactory.com/ffcal_week_this.xml";
            URL url = new URL(xmlUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // Important headers to mimic a real browser
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/115.0 Safari/537.36");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            conn.setRequestProperty("Referer", "https://www.forexfactory.com/");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            InputStream input = conn.getInputStream();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);

            NodeList eventNodes = doc.getElementsByTagName("event");
            for (int i = 0; i < eventNodes.getLength(); i++) {
                Node eventNode = eventNodes.item(i);
                if (eventNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) eventNode;

                    Map<String, String> eventMap = new HashMap<>();
                    eventMap.put("date", getTagValue("date", e));
                    eventMap.put("time", getTagValue("time", e));
                    eventMap.put("currency", getTagValue("currency", e));
                    eventMap.put("impact", getTagValue("impact", e));
                    eventMap.put("title", getTagValue("title", e));

                    eventsList.add(eventMap);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return eventsList;
    }

    private String getTagValue(String tag, Element element) {
        NodeList nl = element.getElementsByTagName(tag);
        if (nl != null && nl.getLength() > 0 && nl.item(0).getFirstChild() != null) {
            return nl.item(0).getFirstChild().getNodeValue();
        }
        return "";
    }

}
