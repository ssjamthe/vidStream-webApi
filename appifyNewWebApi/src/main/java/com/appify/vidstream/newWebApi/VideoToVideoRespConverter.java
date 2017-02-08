package com.appify.vidstream.newWebApi;

import com.appify.vidstream.newWebApi.data.Video;
import org.apache.log4j.Logger;

/**
 * Created by swapnil on 12/01/17.
 */
public class VideoToVideoRespConverter {

    private static final Logger categoryToCategoryRespConverterLogger = Logger.getLogger
            (VideoToVideoRespConverter.class);

    public VideoResp getVideoRespFromVideo(Video video) {
        VideoResp videoResp = new VideoResp();
        videoResp.setId(video.getId());
        videoResp.setName(video.getName());
        return videoResp;
    }
}
