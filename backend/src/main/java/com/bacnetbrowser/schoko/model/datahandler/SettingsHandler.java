package com.bacnetbrowser.schoko.model.datahandler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class handle the settings saved in configuration settings
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@ConfigurationProperties(prefix = "spring.boot.config.settings")
@Component
public class SettingsHandler {

    private String port;
    private String siteName;
    private String siteDescription;
    private String bacnetSeparator;
    private String localDeviceID;
    public static Integer precisionRealValue;


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

    public String getLocalDeviceID() {
        return localDeviceID;
    }

    public void setLocalDeviceID(String localDeviceID) {
        this.localDeviceID = localDeviceID;
    }

    public Integer getPrecisionRealValue() {
        return precisionRealValue;
    }

    public void setPrecisionRealValue(Integer precisionRealValue) {
        SettingsHandler.precisionRealValue = precisionRealValue;
    }

}
