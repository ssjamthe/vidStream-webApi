package com.appify.vidstream.newWebApiTest.data;

import java.util.List;

/**
 * Created by swapnil on 23/11/16.
 */
public class AppInfo {

    private List<Categorization> categorizations;
    private String appId;
    private boolean showBanner;
    private boolean showAdMovingInside;
    private float inmobiAdWeightage;
    private int minIntervalInterstitial;
    private String appBgImageId;
    private int videosPerCall;
    private String noChildrenMsg;


    public List<Categorization> getCategorizations() {

        return categorizations;
    }

    public void setCategorizations(List<Categorization> categorizations) {

        this.categorizations = categorizations;
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

    public float getInmobiAdWeightage() {

        return inmobiAdWeightage;
    }

    public void setInmobiAdWeightage(float inmobiAdWeightage) {

        this.inmobiAdWeightage = inmobiAdWeightage;
    }

    public int getMinIntervalInterstitial() {

        return minIntervalInterstitial;
    }

    public void setMinIntervalInterstitial(int minIntervalInterstitial) {

        this.minIntervalInterstitial = minIntervalInterstitial;
    }

    public String getAppBgImageId() {

        return appBgImageId;
    }

    public void setAppBgImageId(String appBgImageId) {

        this.appBgImageId = appBgImageId;
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

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
