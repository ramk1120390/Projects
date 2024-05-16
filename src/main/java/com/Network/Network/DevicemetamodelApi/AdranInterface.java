package com.Network.Network.DevicemetamodelApi;

import java.util.ArrayList;

public class AdranInterface {
    private ArrayList<Ethernet> ethernetList;
    private ArrayList<GigabitEthernet> gigabitEthernets;
    private ArrayList<T1> t1ArrayList;
    private ArrayList<Fx> fxArrayList;
    private ArrayList<Pri> priArrayList;

    public ArrayList<Ethernet> getEthernetList() {
        return ethernetList;
    }

    public void setEthernetList(ArrayList<Ethernet> ethernetList) {
        this.ethernetList = ethernetList;
    }

    public ArrayList<GigabitEthernet> getGigabitEthernets() {
        return gigabitEthernets;
    }

    public void setGigabitEthernets(ArrayList<GigabitEthernet> gigabitEthernets) {
        this.gigabitEthernets = gigabitEthernets;
    }

    public ArrayList<T1> getT1ArrayList() {
        return t1ArrayList;
    }

    public void setT1ArrayList(ArrayList<T1> t1ArrayList) {
        this.t1ArrayList = t1ArrayList;
    }

    public ArrayList<Fx> getFxArrayList() {
        return fxArrayList;
    }

    public void setFxArrayList(ArrayList<Fx> fxArrayList) {
        this.fxArrayList = fxArrayList;
    }

    public ArrayList<Pri> getPriArrayList() {
        return priArrayList;
    }

    public void setPriArrayList(ArrayList<Pri> priArrayList) {
        this.priArrayList = priArrayList;
    }
}
