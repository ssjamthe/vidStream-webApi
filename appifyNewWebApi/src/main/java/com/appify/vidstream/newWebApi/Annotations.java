package com.appify.vidstream.newWebApi;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by swapnil on 30/11/16.
 */
public class Annotations {

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    @interface CategoryDataLoaders {};

    @Retention(RetentionPolicy.RUNTIME)
    @Qualifier
    public @interface PropertyFilePath {};


}
