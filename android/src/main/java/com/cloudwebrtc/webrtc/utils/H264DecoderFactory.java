package com.cloudwebrtc.webrtc.utils;

import android.media.MediaCodecInfo;
import android.util.Log;

import org.webrtc.EglBase;
import org.webrtc.HardwareVideoDecoderFactory;
import org.webrtc.Predicate;
import org.webrtc.SoftwareVideoDecoderFactory;
import org.webrtc.VideoCodecInfo;
import org.webrtc.VideoDecoder;
import org.webrtc.VideoDecoderFactory;
import org.webrtc.VideoDecoderFallback;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;

/**
 * Created by Minjae Son on 2021-09-01 001.
 */
public class H264DecoderFactory implements VideoDecoderFactory {
    private static String TAG = "H264DecoderFactory";
    private final VideoDecoderFactory hardwareVideoDecoderFactory;
    private final VideoDecoderFactory softwareVideoDecoderFactory = new SoftwareVideoDecoderFactory();

    public H264DecoderFactory(
            EglBase.Context eglContext
    ) {
        this(eglContext, null);
    }

    public H264DecoderFactory(
            EglBase.Context eglContext,
            @Nullable Predicate<MediaCodecInfo> codecAllowedPredicate
    ) {
        Log.d(TAG, "Created with codecAllowedPredicate: " + codecAllowedPredicate);
        hardwareVideoDecoderFactory =
                new HardwareVideoDecoderFactory(eglContext, codecAllowedPredicate);
    }

    @Override
    public VideoDecoder createDecoder(VideoCodecInfo videoCodecInfo) {
        VideoDecoder softwareDecoder = softwareVideoDecoderFactory.createDecoder(videoCodecInfo);
        VideoDecoder hardwareDecoder = hardwareVideoDecoderFactory.createDecoder(videoCodecInfo);
        if(hardwareDecoder != null && softwareDecoder != null){
            return new VideoDecoderFallback(softwareDecoder, hardwareDecoder);
        }
        else if(hardwareDecoder != null){
            return hardwareDecoder;
        }
        else {
            return softwareDecoder;
        }
    }
    
    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        Set<VideoCodecInfo> supportedCodecInfos = new HashSet<>();
        supportedCodecInfos.addAll(Arrays.asList(softwareVideoDecoderFactory.getSupportedCodecs()));
        supportedCodecInfos.addAll(Arrays.asList(hardwareVideoDecoderFactory.getSupportedCodecs()));
        return CodecUtil.filterWith("h264", supportedCodecInfos, TAG);
    }
}
