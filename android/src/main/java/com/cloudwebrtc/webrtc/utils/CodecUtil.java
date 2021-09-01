package com.cloudwebrtc.webrtc.utils;

import android.text.TextUtils;
import android.util.Log;

import org.webrtc.VideoCodecInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Minjae Son on 2021-09-01 001.
 */
class CodecUtil {
    private CodecUtil() { }

    static VideoCodecInfo[] filterWith(String containName, Set<VideoCodecInfo> supportedCodecInfos){
        return filterWith(containName, supportedCodecInfos, "CodecUtil");
    }

    static VideoCodecInfo[] filterWith(String containName, Set<VideoCodecInfo> supportedCodecInfos, String logTag){
        List<VideoCodecInfo> allCodec = new ArrayList<>(supportedCodecInfos);
        ArrayList<VideoCodecInfo> filtered = new ArrayList<>();
        ArrayList<String> allCodecName = new ArrayList<>();
        ArrayList<String> filteredCodecName = new ArrayList<>();
        for(VideoCodecInfo codec : allCodec){
            String name = codec.name;
            allCodecName.add(name);
            if(name.toLowerCase().contains(containName)){
                filteredCodecName.add(name);
                filtered.add(codec);
            }
        }
        Log.d(logTag, "all supported codecs " + TextUtils.join(", ", allCodecName) );
        Log.d(logTag, "forced codecs " + TextUtils.join(", ", filteredCodecName) );
        return filtered.toArray(new VideoCodecInfo[0]);
    }

}
