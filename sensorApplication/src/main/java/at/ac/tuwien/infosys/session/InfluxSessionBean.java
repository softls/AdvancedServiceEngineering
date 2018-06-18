package at.ac.tuwien.infosys.session;

import at.ac.tuwien.infosys.access.InfluxAccess;
import at.ac.tuwien.infosys.entities.SensorDescription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lenaskarlat on 6/16/17.
 */
@Component
@Scope(value="application")
public class InfluxSessionBean {

    private final InfluxAccess influxAccess;

    @Autowired
    public InfluxSessionBean(InfluxAccess influxAccess) {
        this.influxAccess = influxAccess;
    }

    public void sendData(String data){
        this.influxAccess.sendDataFrame(data);
    }

    public List<SensorDescription> getAllSensors(){
        return this.influxAccess.getAllSensors();
    }

    public InfluxAccess getInfluxAccess() {
        return influxAccess;
    }

    public SensorDescription getSensorByName(String sensorName){
        return this.influxAccess.getSensorByName(sensorName);
    }
    public String getSvgByDataFrameId(String sensorName,String timeStamp){
        return this.influxAccess.getSvgByDataFrameId(sensorName,timeStamp);
    }
}


