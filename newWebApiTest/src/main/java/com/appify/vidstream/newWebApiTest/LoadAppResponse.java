package com.appify.vidstream.newWebApiTest;

import com.appify.vidstream.newWebApiTest.data.Tab;

/**
 * Created by swapnil on 29/11/16.
 */
public class LoadAppResponse {

    private TabResp[] tabs;
    private Tab selectedTab;
    private boolean showBanner;
    private boolean showAdMovingInside;
    private float showInmobiAdWeightage;
    private int minIntervalInterstitial;
    private String appBgImage;
    private int videosPerCall;
    private String noChildrenMsg;


    public TabResp[] getTabs() {
        return tabs;
    }

    public void setTabs(TabResp[] tabs) {
        this.tabs = tabs;
    }

    public Tab getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Tab selectedTab) {
        this.selectedTab = selectedTab;
    }

    public boolean isShowBanner() {
        return showBanner;
    }

    public void setShowBanner(boolean showBanner) {
        this.showBanner = showBanner;
    }

    public boolean isShowAdMovingInside() {
        return showAdMovingInside;
    }

    public void setShowAdMovingInside(boolean showAdMovingInside) {
        this.showAdMovingInside = showAdMovingInside;
    }

    public float getShowInmobiAdWeightage() {
        return showInmobiAdWeightage;
    }

    public void setShowInmobiAdWeightage(float showInmobiAdWeightage) {
        this.showInmobiAdWeightage = showInmobiAdWeightage;
    }

    public int getMinIntervalInterstitial() {
        return minIntervalInterstitial;
    }

    public void setMinIntervalInterstitial(int minIntervalInterstitial) {
        this.minIntervalInterstitial = minIntervalInterstitial;
    }

    public String getAppBgImage() {
        return appBgImage;
    }

    public void setAppBgImage(String appBgImage) {
        this.appBgImage = appBgImage;
    }

    public int getVideosPerCall() {
        return videosPerCall;
    }

    public void setVideosPerCall(int videosPerCall) {
        this.videosPerCall = videosPerCall;
    }

    public String getNoChildrenMsg() {
        return noChildrenMsg;
    }

    public void setNoChildrenMsg(String noChildrenMsg) {
        this.noChildrenMsg = noChildrenMsg;
    }
}
