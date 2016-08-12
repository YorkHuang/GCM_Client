package com.grtek.user.gcm_client;

/**
 * Created by user on 2016/8/10.
 */
public class EmgUnit {

    private String mac;
    private String dateTime;
    private String cameraIP;
    private double lat;
    private double lng;

    public EmgUnit(){
    }

    public EmgUnit(String mac, String dateTime, double lat, double lng, String cameraIP){
        this.mac = mac;
        this.dateTime = dateTime;
        this.lat = lat;
        this.lng = lng;
        this.cameraIP = cameraIP;
    }

    public void setMAC(String mac){
        this.mac = mac;
    }

    public void setDateTime(String dt){
        this.dateTime = dt;
    }

    public void setLAT(double lat){
        this.lat = lat;
    }

    public void setLNG(double lng){
        this.lng = lng;
    }

    public void setCameraIP(String cameraIP){
        this.cameraIP = cameraIP;
    }


    public String getMAC(){
        return this.mac;
    }

    public String getDateTime(){
        return this.dateTime;
    }

    public double getLAT(){
        return this.lat;
    }

    public double getLNG(){
        return this.lng;
    }

    public String getCameraIP(){
        return this.cameraIP;
    }

}
