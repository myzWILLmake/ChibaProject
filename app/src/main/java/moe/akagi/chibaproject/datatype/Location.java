package moe.akagi.chibaproject.datatype;

import com.baidu.mapapi.model.inner.GeoPoint;
import com.google.repacked.apache.commons.codec.binary.Base64;

import java.io.Serializable;

/**
 * Created by yunze on 12/1/15.
 */
public class Location implements Serializable{
    private String info;
    private int radius;
    private int direction;
    private double latitude;
    private double longtitude;

    public Location(){
        this.info = "";
    }
    public Location(String info, int radius, int direction, double latitude, double longtitude) {
        this.info = info;
        this.radius = radius;
        this.direction = direction;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getInfo() {
        return info;
    }

    public int getRadius() {
        return radius;
    }

    public int getDirection() {
        return direction;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
}
