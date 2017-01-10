package com.appify.vidstream.newWebApi.data;

import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 23/11/16.
 * TODO: Make this class immutable.
 */
public class AppInfo {

    private List<Categorization> categorizations;
    private String appId;
    private boolean showBanner;
    private boolean showAdMovingInside;
    private float inmobiAdWeightage;
    private int minIntervalInterstitial;
    private String appBgImageURL;
    private int videosPerCall;
    private String noChildrenMsg;
    private Map<String, Categorization> categorizationMap;
    private Map<String, Category> categoryMap;
    private List<String> tokens;
    private List<Category> categorizationsAsCategories;

    public Map<String, Categorization> getCategorizationMap() {
        return categorizationMap;
    }

    public void setCategorizationMap(Map<String, Categorization> categorizationMap) {
        this.categorizationMap = categorizationMap;
    }

    public Map<String, Category> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(Map<String, Category> categoryMap) {
        this.categoryMap = categoryMap;
    }


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


    public int getVideosPerCall() {

        return videosPerCall;
    }

    public void setVideosPerCall(int videosPerCall) {

        this.videosPerCall = videosPerCall;
    }

    public String getNoChildrenMsg() {

        return noChildrenMsg;
    }


    public List<Category> getCategorizationsAsCategories() {
        return categorizationsAsCategories;
    }

    public void setCategorizationsAsCategories(List<Category> categorizationsAsCategories) {
        this.categorizationsAsCategories = categorizationsAsCategories;
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

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
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
