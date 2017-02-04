package com.appify.vidstream.newWebApi;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by swapnil on 29/11/16.
 */
public class LoadAppResponse {

    private boolean showBanner;
    private boolean showAdMovingInside;
    private float showInmobiAdWeightage;
    private int minIntervalInterstitial;
    private String appBgImageURL;
    private int videosPerCall;
    private String noChildrenMsg;
    private CategorizationResp[] categorizations;
    private CategorizationResp selectedCategorization;

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

	public CategorizationResp[] getDefaultCategorization() {
		return categorizations;
	}

	public void setDefaultCategorization(CategorizationResp[] categorizations) {
		this.categorizations = categorizations;
	}
	
	public CategorizationResp[] getCategorizations() {
		return categorizations;
	}

	public void setCategorizations(CategorizationResp[] categorizations) {
		this.categorizations = categorizations;
	}

	public CategorizationResp getSelectedCategorization() {
		return selectedCategorization;
	}

	public void setSelectedCategorization(CategorizationResp selectedCategorization) {
		this.selectedCategorization = selectedCategorization;
	}
	
	
}
