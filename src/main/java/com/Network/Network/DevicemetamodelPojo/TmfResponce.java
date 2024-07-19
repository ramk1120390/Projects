package com.Network.Network.DevicemetamodelPojo;

import java.util.ArrayList;

public class TmfResponce {
    private DeviceDto deviceDto;
    private ArrayList<CardDto> cardDtos;
    private ArrayList<PortDTO> portDTOS;
    private ArrayList<PluggableDTO> pluggableDTOS;
    private ArrayList<LogicalPortDTO> logicalPortDTOS;

    public DeviceDto getDeviceDto() {
        return deviceDto;
    }

    public void setDeviceDto(DeviceDto deviceDto) {
        this.deviceDto = deviceDto;
    }

    public ArrayList<CardDto> getCardDtos() {
        return cardDtos;
    }

    public void setCardDtos(ArrayList<CardDto> cardDtos) {
        this.cardDtos = cardDtos;
    }

    public ArrayList<PortDTO> getPortDTOS() {
        return portDTOS;
    }

    public void setPortDTOS(ArrayList<PortDTO> portDTOS) {
        this.portDTOS = portDTOS;
    }

    public ArrayList<PluggableDTO> getPluggableDTOS() {
        return pluggableDTOS;
    }

    public void setPluggableDTOS(ArrayList<PluggableDTO> pluggableDTOS) {
        this.pluggableDTOS = pluggableDTOS;
    }

    public ArrayList<LogicalPortDTO> getLogicalPortDTOS() {
        return logicalPortDTOS;
    }

    public void setLogicalPortDTOS(ArrayList<LogicalPortDTO> logicalPortDTOS) {
        this.logicalPortDTOS = logicalPortDTOS;
    }

    public TmfResponce() {
    }

    public TmfResponce(DeviceDto deviceDto, ArrayList<CardDto> cardDtos, ArrayList<PortDTO> portDTOS, ArrayList<PluggableDTO> pluggableDTOS, ArrayList<LogicalPortDTO> logicalPortDTOS) {
        this.deviceDto = deviceDto;
        this.cardDtos = cardDtos;
        this.portDTOS = portDTOS;
        this.pluggableDTOS = pluggableDTOS;
        this.logicalPortDTOS = logicalPortDTOS;
    }
}
