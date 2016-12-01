package com.appify.vidstream.newWebApiTest.data;

import com.appify.vidstream.newWebApiTest.VideoResp;

import java.util.List;

/**
 * Created by swapnil on 01/12/16.
 */
public class VideoToVideoRespConverter {

    public VideoResp[] getVideoRespArray(List<Video> videoList){

        VideoResp[] videoResps = new VideoResp[videoList.size()];
        Video video = new Video();

        for(int i=0;i<videoList.size();i++){
            VideoResp videoResp = new VideoResp();
            video = videoList.get(i);
            videoResp.setId(video.getId());
            videoResp.setName(video.getName());
            videoResps[i] = videoResp;
        }

        return videoResps;
    }
}
