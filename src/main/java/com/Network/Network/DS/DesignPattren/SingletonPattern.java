package com.Network.Network.DS.DesignPattren;

import java.io.Serializable;

public class SingletonPattern implements Serializable {
    private String Name;
    private String Desc;

    private static SingletonPattern singletonPattern = null;

    SingletonPattern() {

    }

    public static SingletonPattern getInstance() {
        if (singletonPattern == null) {
            singletonPattern = new SingletonPattern();
        }
        return singletonPattern;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }
}
