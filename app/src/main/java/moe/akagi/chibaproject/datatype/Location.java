package moe.akagi.chibaproject.datatype;

import java.io.Serializable;

/**
 * Created by yunze on 12/1/15.
 */
public class Location implements Serializable{
    String name;
    int radius;
    int direction;
    double latitude;
    double longtitude;

    public Location(){
        this.name = "";
    }

    public Location(String name, double latitude, double longtitude) {
        this.name = name;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public Location(String name, int radius, int direction, double latitude, double longtitude) {
        this.name = name;
        this.radius = radius;
        this.direction = direction;
        this.latitude = latitude;
        this.longtitude = longtitude;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
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
