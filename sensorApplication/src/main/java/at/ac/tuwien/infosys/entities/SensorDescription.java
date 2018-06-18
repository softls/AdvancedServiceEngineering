package at.ac.tuwien.infosys.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lenaskarlat on 5/24/17.
 */
public class SensorDescription {

    private String id;

    private boolean isTurnedOff=true;

    private String sensorName;
    private String sensorType;


    private int xCoordinate;
    private int yCoordinate;
    private int zCoordinate;

    private int xTransformed;
    private int yTransformed;

    private List<DataFrame> dataFrames=new LinkedList<>();

    public SensorDescription() {

    }

    public SensorDescription(boolean isTurnedOff, String sensorName, String sensorType, int xCoordinate, int yCoordinate, int zCoordinate) {
        this.id=sensorName;
        this.isTurnedOff = isTurnedOff;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.zCoordinate = zCoordinate;
        calculateTransformedCoordinates(xCoordinate,yCoordinate);

    }

    public SensorDescription(boolean isTurnedOff, String sensorName, String sensorType) {
        this.id=sensorName;
        this.isTurnedOff = isTurnedOff;
        this.sensorName = sensorName;
        this.sensorType = sensorType;
    }

    private void calculateTransformedCoordinates(int xCoordinate, int yCoordinate) {
        int height = 252;
        int width = 535;
        int x_max = 47680;
        int x_min = -35340;
        int y_max=10840;
        int y_min = -25735;
        double coefX = width/83.02;
        double coefY=height/36.575;


        if (xCoordinate>=0){
            setxTransformed((int)(((-1)*x_min+xCoordinate)*coefX/1000));
            if (yCoordinate>=0){
                setyTransformed((int)((y_max-yCoordinate)*coefY/1000));
            }
            else setyTransformed((int)((y_max+(-1)*yCoordinate)*coefY/1000));
        }
        else {
            setxTransformed((int)(((-1)*x_min-(-1)*xCoordinate)*coefX/1000));
            if (yCoordinate>=0){
                setyTransformed((int)((y_max-yCoordinate)*coefY/1000));
            }
            else setyTransformed((int)((y_max+(-1)*yCoordinate)*coefY/1000));

        }
     //   System.out.println(xCoordinate+"->"+getxTransformed()+". "+yCoordinate+"->"+ getyTransformed());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isTurnedOff() {
        return isTurnedOff;
    }

    public void setTurnedOff(boolean turnedOff) {
        isTurnedOff = turnedOff;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getzCoordinate() {
        return zCoordinate;
    }

    public void setzCoordinate(int zCoordinate) {
        this.zCoordinate = zCoordinate;
    }

    public List<DataFrame> getDataFrames() {
        return dataFrames;
    }

    public void setDataFrames(List<DataFrame> dataFrames) {
        this.dataFrames = dataFrames;
    }

    public int getxTransformed() {
        return xTransformed;
    }

    public void setxTransformed(int xTransformed) {
        this.xTransformed = xTransformed;
    }

    public int getyTransformed() {
        return yTransformed;
    }

    public void setyTransformed(int yTransformed) {
        this.yTransformed = yTransformed;
    }
}
