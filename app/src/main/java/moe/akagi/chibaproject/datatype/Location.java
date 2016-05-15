package moe.akagi.chibaproject.datatype;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import java.io.Serializable;

import moe.akagi.chibaproject.BR;


/**
 * Created by yunze on 12/1/15.
 */
public class Location extends BaseObservable implements Serializable{
    String name = "";
    int radius;
    int direction;
    double latitude;
    double longitude;

    public Location(){
    }

    public Location(String name, double latitude, double longtitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longtitude;
    }

    public Location(String name, int radius, int direction, double latitude, double longtitude) {
        this.name = name;
        this.radius = radius;
        this.direction = direction;
        this.latitude = latitude;
        this.longitude = longtitude;
    }

    public void copyConstruct(Location location) {
        setName(location.name);
        setDirection(location.direction);
        setLatitude(location.latitude);
        setLongitude(location.longitude);
    }

    @Bindable
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

    public double getLongitude() {
        return longitude;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
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

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

}
