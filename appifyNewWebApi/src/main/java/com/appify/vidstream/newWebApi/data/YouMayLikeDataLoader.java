package com.appify.vidstream.newWebApi.data;

import java.util.List;

/**
 * Created by swapnil on 16/01/17.
 */
public interface YouMayLikeDataLoader {
    public void updateYouMayLikeVideos(String originalVideoId,List<String> listOfSimilarVideoIds);
    public List<String> extractSimilarVideiosFromYoutube(String videoId);
}
