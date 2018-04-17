package org.dan.entity;

import java.io.Serializable;

public class LogMessage implements Serializable {

    private int type;//1浏览日志，2点击日志，3搜索日志，4购买日志
    private String hrefTag;
    private String hrefContent;
    private String referrerUrl;
    private String requestUrl;
    private String clickTime;
    private String appName;//浏览器类型
    private String appVersion;//浏览器版本
    private String language;//浏览器语言
    private String platform;//操作系统
    private String screen;//屏幕尺寸
    private String coordinate;//产生点击流的系统编号
    private String userName;

    public LogMessage(int type, String referrerUrl, String requestUrl, String userName) {
        this.type = type;
        this.referrerUrl = referrerUrl;
        this.requestUrl = requestUrl;
        this.userName = userName;
    }

    public String getCompareFieldValue(String field) {
        if("hrefTag".equalsIgnoreCase(field)){
            return hrefTag;
        } else if("referrerUrl".equalsIgnoreCase(field)) {
            return referrerUrl;
        } else if("requestUrl".equalsIgnoreCase(field)) {
            return requestUrl;
        }
        return "";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getHrefTag() {
        return hrefTag;
    }

    public void setHrefTag(String hrefTag) {
        this.hrefTag = hrefTag;
    }

    public String getHrefContent() {
        return hrefContent;
    }

    public void setHrefContent(String hrefContent) {
        this.hrefContent = hrefContent;
    }

    public String getReferrerUrl() {
        return referrerUrl;
    }

    public void setReferrerUrl(String referrerUrl) {
        this.referrerUrl = referrerUrl;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getClickTime() {
        return clickTime;
    }

    public void setClickTime(String clickTime) {
        this.clickTime = clickTime;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
