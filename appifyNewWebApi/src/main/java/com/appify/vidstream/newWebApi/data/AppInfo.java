package com.appify.vidstream.newWebApi.data;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.List;
import java.util.Map;

/**
 * Created by swapnil on 23/11/16.
 * TODO: Make this class immutable.
 */
public class AppInfo {

    private ImmutableList<Categorization> categorizations;
    private String appId;
    private boolean showBanner;
    private boolean showAdMovingInside;
    private float inmobiAdWeightage;
    private int minIntervalInterstitial;
    private String appBgImageURL;
    private int videosPerCall;
    private String noChildrenMsg;
    private ImmutableMap<String, Categorization> categorizationMap;
    private ImmutableMap<String, Category> categoryMap;
    private ImmutableList<String> tokens;
    private ImmutableList<Category> categorizationsAsCategories;

    public ImmutableMap<String, Categorization> getCategorizationMap() {
        return categorizationMap;
    }

    public void setCategorizationMap(ImmutableMap<String, Categorization> categorizationMap) {
        this.categorizationMap = categorizationMap;
    }

    public ImmutableMap<String, Category> getCategoryMap() {
        return categoryMap;
    }

    public void setCategoryMap(ImmutableMap<String, Category> categoryMap) {
        this.categoryMap = categoryMap;
    }


    public ImmutableList<Categorization> getCategorizations() {

        return categorizations;
    }

    public void setCategorizations(ImmutableList<Categorization> categorizations) {

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


    public ImmutableList<Category> getCategorizationsAsCategories() {
        return categorizationsAsCategories;
    }

    public void setCategorizationsAsCategories(ImmutableList<Category> categorizationsAsCategories) {
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

    public void setTokens(ImmutableList<String> tokens) {
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
