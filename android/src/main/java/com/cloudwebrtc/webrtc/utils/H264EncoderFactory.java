package com.cloudwebrtc.webrtc.utils;

import android.util.Log;

import org.webrtc.EglBase;
import org.webrtc.HardwareVideoEncoderFactory;
import org.webrtc.SoftwareVideoEncoderFactory;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoEncoder;
import org.webrtc.VideoEncoderFactory;
import org.webrtc.VideoEncoderFallback;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Minjae Son on 2021-09-01 001.
 */
public class H264EncoderFactory implements VideoEncoderFactory {
    private static String TAG = "H264EncoderFactory";
    private final VideoEncoderFactory hardwareVideoEncoderFactory;
    private final VideoEncoderFactory softwareVideoEncoderFactory = new SoftwareVideoEncoderFactory();

    public H264EncoderFactory(
            EglBase.Context eglContext,
            boolean enableIntelVp8Encoder,
            boolean enableH264HighProfile
    ) {
        Log.d(TAG, "Created with enableIntelVp8Encoder: " + enableIntelVp8Encoder + ", enableH264HighProfile: " + enableH264HighProfile);
        hardwareVideoEncoderFactory =
                new HardwareVideoEncoderFactory(eglContext, enableIntelVp8Encoder, enableH264HighProfile);
    }

    @Override
    public VideoEncoder createEncoder(VideoCodecInfo videoCodecInfo) {
        VideoEncoder softwareEncoder = softwareVideoEncoderFactory.createEncoder(videoCodecInfo);
        VideoEncoder hardwareEncoder = hardwareVideoEncoderFactory.createEncoder(videoCodecInfo);
        if(hardwareEncoder != null && softwareEncoder != null){
            return new VideoEncoderFallback(softwareEncoder, hardwareEncoder);
        }
        else if(hardwareEncoder != null){
            return hardwareEncoder;
        }
        else {
            return softwareEncoder;
        }
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        Set<VideoCodecInfo> supportedCodecInfos = new HashSet<>();
        supportedCodecInfos.addAll(Arrays.asList(softwareVideoEncoderFactory.getSupportedCodecs()));
        supportedCodecInfos.addAll(Arrays.asList(hardwareVideoEncoderFactory.getSupportedCodecs()));
        return CodecUtil.filterWith("h264", supportedCodecInfos, TAG);
    }

}
