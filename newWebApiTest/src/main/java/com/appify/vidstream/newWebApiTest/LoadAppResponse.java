package com.appify.vidstream.newWebApiTest;

/**
 * Created by swapnil on 29/11/16.
 */
public class LoadAppResponse {

    private TabResp[] tabs;
    private TabResp selectedTab;
    private boolean showBanner;
    private boolean showAdMovingInside;
    private float showInmobiAdWeightage;
    private int minIntervalInterstitial;
    private String appBgImageURL;
    private int videosPerCall;
    private String noChildrenMsg;


    public com.appify.vidstream.newWebApiTest.TabResp[] getTabs() {
        return tabs;
    }

    public void setTabs(com.appify.vidstream.newWebApiTest.TabResp[] tabs) {
        this.tabs = tabs;
    }

    public TabResp getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(TabResp selectedTab) {
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

	/**
	 * @return the appBgImageURL
	 */
	public String getAppBgImageURL() {
		return appBgImageURL;
	}

	/**
	 * @param appBgImageURL the appBgImageURL to set
	 */
	public void setAppBgImageURL(String appBgImageURL) {
		this.appBgImageURL = appBgImageURL;
	}
}
