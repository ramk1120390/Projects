package com.Network.Network.DS.DesignPattren.BridgeDesign;

public class UltraHDProcess implements VideoProcessor {
    @Override
    public void process(String videoFile) {
        System.out.println("Video Process is UltraHD" + videoFile);
    }
}
