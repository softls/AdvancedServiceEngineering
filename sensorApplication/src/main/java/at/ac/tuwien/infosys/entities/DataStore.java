package at.ac.tuwien.infosys.entities;

import at.ac.tuwien.infosys.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

/**
 * Created by lenaskarlat on 4/19/17.
 */

public class DataStore {

    private static DataStore instance = null;

    private Map<String, List<DataFrame>> sensorDataFrameMap = new HashMap();
    private List<SensorDescription> sensors = new ArrayList<>();

    protected DataStore() {
        createAllSensors();
    }

    public static DataStore getInstance() {
        if (instance == null) {
            instance = new DataStore();
        }
        return instance;
    }

    public List<String> getTimeStampsList(String sensorId) {
        List<DataFrame> dataFrameList = sensors.get(Integer.parseInt(sensorId)).getDataFrames();
            List timeStampsList = new ArrayList<>();
            for (DataFrame dataFrame : dataFrameList) {
                timeStampsList.add(dataFrame.getTimeStamp());
            }
            Collections.sort(timeStampsList);
            return timeStampsList;
    }


    public void putSensorDataFrame(String sensorId, String timeStamp, String data) {
        DataFrame dataFrame = null;
        try {
            dataFrame = new DataFrame(sensorId, timeStamp, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (sensorDataFrameMap.containsKey(sensorId)) {
            sensorDataFrameMap.get(sensorId).add(dataFrame);
        } else {
            List<DataFrame> dataFrameList = new LinkedList<>();
            dataFrameList.add(dataFrame);
            sensorDataFrameMap.put(sensorId, dataFrameList);
        }
        for (SensorDescription sensor: sensors){
            if (sensor.getId().equals(sensorId))
            {
                sensor.getDataFrames().add(dataFrame);
                break;
            }
        }

    }

    public String getSensorDataFrame(String sensorId, String timeStamp) {

        List<DataFrame> dataFrameList = sensors.get(Integer.parseInt(sensorId)-1).getDataFrames();
        for (DataFrame dataFrame : dataFrameList) {
            if (dataFrame.getTimeStamp().equals(timeStamp))
                return dataFrame.getData();
        }
        return "no data";
    }

    public void createAllSensors() {
     //   List<SensorDescription> sensors = new ArrayList<>();
        SensorDescription sensor1 = new SensorDescription(false, "A1", "Asus - Kinect", 45430, -17350, 3960);
        sensor1.setId("1");
        sensors.add(sensor1);
        SensorDescription sensor2 = new SensorDescription(false, "A2", "Asus - Kinect", 38170, -23085, 4050);
        sensor2.setId("2");
        sensors.add(sensor2);
        SensorDescription sensor3 = new SensorDescription(false, "A3", "Asus - Kinect", 36545, -17485, 4035);
        sensor3.setId("3");
        sensors.add(sensor3);
        SensorDescription sensor4 = new SensorDescription(false, "A4", "Asus - Kinect", 7335, -2605, 4295);
        sensor4.setId("4");
        sensors.add(sensor4);
        SensorDescription sensor5 = new SensorDescription(false, "A5", "Asus - Kinect", 2815, 7230, 3870);
        sensor5.setId("5");
        sensors.add(sensor5);
        SensorDescription sensor6 = new SensorDescription(false, "A6", "Asus - Kinect", -5130, 455, 4445);
        sensor6.setId("6");
        sensors.add(sensor6);
        SensorDescription sensor7 = new SensorDescription(false, "A7", "Asus - Kinect", -1755, -4165, 3915);
        sensor7.setId("7");
        sensors.add(sensor7);
        SensorDescription sensor8 = new SensorDescription(false, "A8", "Asus - Kinect", -5210, -8540, 4340);
        sensor8.setId("8");
        sensors.add(sensor8);
        SensorDescription sensor9 = new SensorDescription(false, "A9", "Asus - Kinect", -20905, 1775, 4530);
        sensor9.setId("9");
        sensors.add(sensor9);
        SensorDescription sensor10 = new SensorDescription(false, "A10", "Asus - Kinect", 3410, 1320, 4220);
        sensor10.setId("10");
        sensors.add(sensor10);
        SensorDescription sensor11 = new SensorDescription(false, "A11", "Asus - Kinect", -32145, 1875, 4230);
        sensor11.setId("11");
        sensors.add(sensor11);

    }

    public List<SensorDescription> getAllSensors() {
        return sensors;
    }

    public SensorDescription getSensorById(String sensorName){
        for (SensorDescription sensor: sensors){
            if (sensor.getId().equals(sensorName))
            return sensor;
        }
        return null;

    }

}