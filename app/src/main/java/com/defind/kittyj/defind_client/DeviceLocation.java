package com.defind.kittyj.defind_client;

import java.util.Date;

/**
 * Created by kittyj on 1/11/16.
 */
public class DeviceLocation {

    private String device;
    private String lat;
    private String lng;
    private Date time;

    public DeviceLocation(String inDevice, String inLat, String inLng, Date inTime) {

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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
