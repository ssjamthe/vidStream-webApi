package com.appify.vidstream.newWebApi;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Created by swapnil on 29/11/16.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
public class VideoResp {

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
