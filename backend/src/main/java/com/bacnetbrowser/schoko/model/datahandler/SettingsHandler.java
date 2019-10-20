package com.bacnetbrowser.schoko.model.datahandler;


import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

import java.util.LinkedList;
import java.util.Set;

/**
 * This class handel defaultSettings done in the browser
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class SettingsHandler {

    private Integer port;
    private String siteName;
    private String siteDescription;
    private String bacnetSeparator;


    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    public String getBacnetSeparator() {
        return bacnetSeparator;
    }

    public void setBacnetSeparator(String bacnetSeparator) {
        this.bacnetSeparator = bacnetSeparator;
    }

    public void readXMLSettings() {
        try {
        File fXmlFile = new File("C:\\Users\\admin\\IdeaProjects\\schoko\\backend\\src\\main\\resources\\defaultSettings");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);

        setPort(Integer. parseInt((doc.getElementsByTagName("port").item(0).getTextContent())));
        setSiteName(doc.getElementsByTagName("siteName").item(0).getTextContent());
        setSiteDescription(doc.getElementsByTagName("siteDescription").item(0).getTextContent());
        setBacnetSeparator(doc.getElementsByTagName("bacnetSeparator").item(0).getTextContent());

    } catch (Exception e){
        e.printStackTrace();}
    }

    public void writeXMLSettings() {
        try {
            File fXmlFile = new File("../../defaultSettings");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getElementsByTagName("port").item(0).setNodeValue(getPort().toString());
            doc.getElementsByTagName("siteName").item(0).setNodeValue(getSiteName());
            doc.getElementsByTagName("siteDescription").item(0).setNodeValue(getSiteDescription());
            doc.getElementsByTagName("bacnetSeparator").item(0).setNodeValue(getBacnetSeparator());

        } catch (Exception e){
            e.printStackTrace();}
    }
}
