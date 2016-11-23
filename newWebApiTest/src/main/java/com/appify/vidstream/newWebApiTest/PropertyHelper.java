package com.appify.vidstream.newWebApiTest;

/**
 * Created by swapnil on 23/11/16.
 */
public interface PropertyHelper {

    String getStringProperty(String propName,String defaultVal);

    Integer getIntProperty(String propName,Integer defaultVal);

    Boolean getBooleanProperty(String propName,Boolean defaultVal);

    Double getDoubleProperty(String propName,Double defaultVal);

}
