package com.Network.Network.DS.DesignPattren;

public class PatternCheck {
    public static void main(String[] args) {
        SingletonPattern pattern=new SingletonPattern();
        pattern.setName("Ramkumar");
        pattern.setDesc("Test");
        SingletonPattern pattern1=new SingletonPattern();
        pattern1.setName("Test1");
        pattern1.setDesc("Desc");
        SingletonPattern pattern2=SingletonPattern.getInstance();
        SingletonPattern pattern3=SingletonPattern.getInstance();
        System.out.println(pattern2+" "+pattern3);
//com.Network.Network.DS.DesignPattren.SingletonPattern@1cf4f579 com.Network.Network.DS.DesignPattren.SingletonPattern@1cf4f579
    }
}
