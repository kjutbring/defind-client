package com.defind.kittyj.defind_client;

import java.util.Date;

/**
 * Created by kittyj on 1/11/16.
 */
public class DeviceLocation {

    private String device;
    private Double lat;
    private Double lng;
    private Date time;

    public DeviceLocation(String inDevice, Double inLat, Double inLng, Date inTime) {

        device = inDevice;
        lat = inLat;
        lng = inLng;
        time = inTime;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
