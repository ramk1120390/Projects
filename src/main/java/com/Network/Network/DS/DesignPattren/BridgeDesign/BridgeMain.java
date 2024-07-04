package com.Network.Network.DS.DesignPattren.BridgeDesign;

public class BridgeMain {
    public static void main(String[] args) {
        Video youtubeVideo = new YoutubeVideo(new HDProcess());
        youtubeVideo.play("abc.mp4");
        Video netflixVideo = new NetflixVideo(new UltraHDProcess());
        netflixVideo.play("abc.mp4");
    }
}
