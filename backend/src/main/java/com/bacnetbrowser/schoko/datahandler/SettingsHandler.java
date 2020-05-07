package com.bacnetbrowser.schoko.datahandler;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * This class handle the settings saved in configuration settings
 * @author Vogt Andreas,Daniel Reiter, Rafael Grimm
 * @version 1.0
 */
@ConfigurationProperties(prefix = "settings")
@Component
public class SettingsHandler {

    public static String port;
    public static String siteName;
    public static String siteDescription;
    public static String bacnetSeparator;
    public static String localDeviceID;
    public static Integer precisionRealValue;
    public static Integer scanSeconds = 5;



    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        SettingsHandler.port = port;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        SettingsHandler.siteName = siteName;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        SettingsHandler.siteDescription = siteDescription;
    }

    public String getBacnetSeparator() {
        return bacnetSeparator;
    }

    public void setBacnetSeparator(String bacnetSeparator) {
        SettingsHandler.bacnetSeparator = bacnetSeparator;
    }

    public String getLocalDeviceID() {
        return localDeviceID;
    }

    public void setLocalDeviceID(String localDeviceID) {
        SettingsHandler.localDeviceID = localDeviceID;
    }

    public Integer getPrecisionRealValue() {
        return precisionRealValue;
    }

    public void setPrecisionRealValue(Integer precisionRealValue) {
        SettingsHandler.precisionRealValue = precisionRealValue;
    }

    public static Integer getScanSeconds() {
        return scanSeconds;
    }

    public static void setScanSeconds(Integer scanSeconds) {
        SettingsHandler.scanSeconds = scanSeconds;
    }
}
