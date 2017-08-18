package com.tianzl.androidvideo.utils;

import com.tianzl.androidvideo.R;
import com.tianzl.androidvideo.entity.Video;
import com.tianzl.androidvideo.entity.VideoInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tianzl on 2017/8/6.
 */

public class Constant {
    //网络视频地址：
    public static String[] videoArray=new String[]{
            "http://mediadownloads.mlb.com/mlbam/2009/05/09/mlbtv_tbabos_4494731_1m.mp4",
            "https://media.w3.org/2010/05/sintel/trailer.mp4",
            "http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"
    };
    public static int[] img=new int[]{R.mipmap.video_1,R.mipmap.video_2,R.mipmap.video_3};
    public static String [] name=new String[]{
            "MLB.com,比赛激烈开开赛",
            "驯龙高手，一段人与龙之间的爱恨纠葛",
            "冰河世纪，傻鸟"
    };
    public static List<VideoInfo> getVideoInfo(){
        List<VideoInfo> list=new ArrayList<>();
        list.add(new VideoInfo(videoArray[0],name[0],img[0]));
        list.add(new VideoInfo(videoArray[1],name[1],img[1]));
        list.add(new VideoInfo(videoArray[2],name[2],img[2]));
        return list;
    }
}
