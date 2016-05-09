package moe.akagi.chibaproject.datatype;

import com.baidu.mapapi.model.inner.GeoPoint;
import com.google.repacked.apache.commons.codec.binary.Base64;

import java.io.Serializable;

/**
 * Created by yunze on 12/1/15.
 */
public class Location implements Serializable{
    private String info;
    private GeoPoint point = null;

    public Location(){
        this.info = "";
    }
    public Location(String info){
        this.info = info;
    }
    public Location(String info,double latitude, double longtitude) {
        this.info = info;
        this.point = new GeoPoint(latitude * 1E6, longtitude * 1E6);
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public GeoPoint getPoint() {
        return point;
    }

    public void setPoint(GeoPoint point) {
        this.point = point;
    }

    public void setPoint(double latitude, double longtitude) {
        this.point = new GeoPoint(latitude * 1E6, longtitude * 1E6);
    }
}
