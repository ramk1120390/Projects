package com.Network.Network.DS.DesignPattren.BridgeDesign;

public class YoutubeVideo extends Video{
    public YoutubeVideo(VideoProcessor processor) {
        super(processor);
    }

    @Override
    public void play(String videoFile) {
        processor.process(videoFile);

    }
}