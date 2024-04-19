package com.Network.Network.DevicemetamodelPojo;

import java.util.ArrayList;

public class CienaNcm {
    private CienaInterface cienaInterface;
    private ArrayList<CienaPort> cienaPorts;

    public CienaNcm() {
    }

    public CienaNcm(CienaInterface cienaInterface, ArrayList<CienaPort> cienaPorts) {
        this.cienaInterface = cienaInterface;
        this.cienaPorts = cienaPorts;
    }

    public CienaInterface getCienaInterface() {
        return cienaInterface;
    }

    public void setCienaInterface(CienaInterface cienaInterface) {
        this.cienaInterface = cienaInterface;
    }

    public ArrayList<CienaPort> getCienaPorts() {
        return cienaPorts;
    }

    public void setCienaPorts(ArrayList<CienaPort> cienaPorts) {
        this.cienaPorts = cienaPorts;
    }
}
