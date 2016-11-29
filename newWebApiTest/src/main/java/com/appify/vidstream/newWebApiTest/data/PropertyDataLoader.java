package com.appify.vidstream.newWebApiTest.data;

import java.util.Map;

/**
 * Created by swapnil on 30/11/16.
 */
public interface PropertyDataLoader {

    void startLoading();

    void stopLoading();

    Map<String, String> getProps();
}
