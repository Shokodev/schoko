package com.bacnetbrowser.schoko.model.datahandler;


import org.springframework.stereotype.Component;

/**
 * This class handel settings done in the browser
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@Component
public class SettingsHandler {

    private Integer port = 0xBAC0;
    private String siteName = "Anlage";
    private String siteDescription = "Anlage Kunde xy";
    private String bacnetSeparator = "'";

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
}
