package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.Link;
import com.appify.vidstream.newWebApi.data.Video;

/**
 * Created by swapnil on 05/02/17.
 */
public class LinkToLinkRespConverter {

    public LinkResp getLinkRespFromLink(Link link) {
        LinkResp linkResp = new LinkResp();
        linkResp.setId(link.getId());
        linkResp.setName(link.getName());
        linkResp.setLinkUrl(link.getLinkUrl());
        linkResp.setImg(link.getImageURL());
        return linkResp;
    }
}
