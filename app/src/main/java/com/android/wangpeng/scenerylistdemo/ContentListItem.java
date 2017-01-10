package com.android.wangpeng.scenerylistdemo;

/**
 * Created by Mr_wang on 2016/12/17.
 */

public class ContentListItem {
    private String sceneryImageUrl;
    private String sceneryTitle;
    private String sceneryGrade;
    private String sceneryAddress;
    private String sceneryDetailUrl;

    public ContentListItem (String sceneryImageUrl, String sceneryTitle, String sceneryGrade, String sceneryAddress, String sceneryDetailUrl){
        this.sceneryImageUrl = sceneryImageUrl;
        this.sceneryTitle = sceneryTitle;
        this.sceneryGrade = sceneryGrade;
        this.sceneryAddress = sceneryAddress;
        this.sceneryDetailUrl = sceneryDetailUrl;

    }

    public String getSceneryDetailUrl() {
        return sceneryDetailUrl;
    }

    public void setSceneryDetailUrl(String sceneryDetailUrl) {
        this.sceneryDetailUrl = sceneryDetailUrl;
    }
    public String getSceneryImageUrl() {
        return sceneryImageUrl;
    }

    public void setSceneryImageUrl(String sceneryImageUrl) {
        this.sceneryImageUrl = sceneryImageUrl;
    }

    public String getSceneryTitle() {
        return sceneryTitle;
    }

    public void setSceneryTitle(String sceneryTitle) {
        this.sceneryTitle = sceneryTitle;
    }

    public String getSceneryGrade() {
        return sceneryGrade;
    }

    public void setSceneryGrade(String sceneryGrade) {
        this.sceneryGrade = sceneryGrade;
    }

    public String getSceneryAddress() {
        return sceneryAddress;
    }

    public void setSceneryAddress(String sceneryAddress) {
        this.sceneryAddress = sceneryAddress;
    }
}
