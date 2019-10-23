package com.bacnetbrowser.schoko.model.datahandler;


import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;


/**
 * This class handel defaultSettings done in the browser
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class SettingsHandler {

    private String port;
    private String siteName;
    private String siteDescription;
    private String bacnetSeparator;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
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
        File xml = getXmlSettingsFile();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xml);

        setPort((doc.getElementsByTagName("port").item(0).getTextContent()));
        setSiteName(doc.getElementsByTagName("siteName").item(0).getTextContent());
        setSiteDescription(doc.getElementsByTagName("siteDescription").item(0).getTextContent());
        setBacnetSeparator(doc.getElementsByTagName("bacnetSeparator").item(0).getTextContent());

    } catch (Exception e){
            System.err.println("Can't read XML settings");;}
    }

    public void writeXMLSettings() {
        try {
            File xml = getXmlSettingsFile();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xml);

            doc.getElementsByTagName("port").item(0).setTextContent(getPort());
            doc.getElementsByTagName("siteName").item(0).setTextContent(getSiteName());
            doc.getElementsByTagName("siteDescription").item(0).setTextContent(getSiteDescription());
            doc.getElementsByTagName("bacnetSeparator").item(0).setTextContent(getBacnetSeparator());

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(xml);

            Source input = new DOMSource(doc);
            transformer.transform(input, output);


        } catch (Exception e){
            System.err.println("Can't write XML settings");
            ;}
    }

    private File getXmlSettingsFile(){
        File xml = new File("src\\main\\resources\\defaultSettings");
        if(!xml.exists()){
            return new File("backend\\src\\main\\resources\\defaultSettings");
        }
        return xml;
    }
}
