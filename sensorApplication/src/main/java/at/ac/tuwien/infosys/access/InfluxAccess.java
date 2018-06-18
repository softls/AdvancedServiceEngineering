package at.ac.tuwien.infosys.access;

import at.ac.tuwien.infosys.entities.DataFrame;
import at.ac.tuwien.infosys.entities.SensorDescription;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenaskarlat on 6/15/17.
 */
@Service
public class InfluxAccess {

    private InfluxDB influxDB;
    private String databaseMain;
    private String databaseSeries;
    private String databaseSeriesProcessed;

    @Autowired
    public InfluxAccess(@Value("${influxEndpoint}") String influxEndpoint, @Value("${databaseName}") String databaseName,@Value("${influxUser}") String influxUser, @Value("${influxPassword}") String influxPassword) {
       this.influxDB = InfluxDBFactory.connect(influxEndpoint, influxUser,influxPassword);
       this.databaseMain = databaseName;
       this.databaseSeries = databaseName+"Series";
       this.databaseSeriesProcessed = databaseName+"SeriesProcessed";

    }

    //get all sensors from sensors table in PositioningData
    public List<SensorDescription> getAllSensors(){
        Query query = new Query("SELECT * FROM sensor", databaseMain);
        QueryResult queryResult = influxDB.query(query);
        List<Result> results = queryResult.getResults();
        Series series = results.get(0).getSeries().get(0);
        List<List<Object>> values = series.getValues();
        List<String> columns = series.getColumns();
        List<SensorDescription> sensorDescriptionList = new ArrayList<>();
        for (List<Object> objectList:values) {
            String sensorName ="";
            String sensorType="";
            Boolean isTurnedOff=false;
            int x=0;
            int y=0;
            int z=0;
            for (int i=0; i<columns.size(); i++) {
                if (columns.get(i).equals("id"))  sensorName = objectList.get(i).toString();
                if (columns.get(i).equals("value"))  { if (objectList.get(i).toString().equals("OFF")) isTurnedOff=true;};
                if (columns.get(i).equals("type")) sensorType = objectList.get(i).toString();
                if (columns.get(i).equals("x")) x= Integer.valueOf(objectList.get(i).toString());
                if (columns.get(i).equals("y")) y=Integer.valueOf(objectList.get(i).toString());
                if (columns.get(i).equals("z")) z=Integer.valueOf(objectList.get(i).toString());
            }
            SensorDescription sensor = new SensorDescription(isTurnedOff, sensorName,sensorType,x,y,z);
            sensorDescriptionList.add(sensor);
        }
        return sensorDescriptionList;
    }

    //sends raw data frame to influx access
    public void sendDataFrame(String measurement){
        JSONObject jsonObject = new JSONObject(measurement);
        String rawDataFrame = jsonObject.getString("dataFrameMessage");
        String[] measurementArray = rawDataFrame.split(";");
        Long timeConverted = Long.parseLong(measurementArray[1]);
        Point point1 = Point.measurement(measurementArray[0])
                .time(timeConverted, TimeUnit.NANOSECONDS)
                .addField("data", measurementArray[2])
                .build();
        influxDB.write(databaseSeries, "autogen", point1);
    };

    //gets the sensor and according data frame
    public SensorDescription getSensorByName(String sensorName){
        Query query = new Query("SELECT * FROM sensor WHERE id='"+sensorName+"'", databaseMain);
        QueryResult queryResult = influxDB.query(query);
        List<Result> results = queryResult.getResults();
        Series series = results.get(0).getSeries().get(0);
        List<List<Object>> values = series.getValues();
        List<String> columns = series.getColumns();
        SensorDescription sensor=null;
        for (List<Object> objectList:values) {
            String sensorType="";
            for (int i=0; i<columns.size(); i++) {
                if (columns.get(i).equals("type")) {sensorType = objectList.get(i).toString(); break;}
            }
            sensor = new SensorDescription(false, sensorName,sensorType);
        }
        List<DataFrame> dataFrameList = getDataFramesBySensor(sensorName);
        sensor.setDataFrames(dataFrameList);
        return sensor;
        }

    public List<DataFrame> getDataFramesBySensor(String sensorName){
        Query queryDataFrames = new Query("SELECT * FROM sensor_"+sensorName, databaseSeriesProcessed);
        QueryResult queryDataFramesResult = influxDB.query(queryDataFrames);
        List<Result> dataFramesResults = queryDataFramesResult.getResults();
        Series seriesDataFrames = dataFramesResults.get(0).getSeries().get(0);
        List<List<Object>> valuesDataFrames = seriesDataFrames.getValues();
        List<String> columns1 = seriesDataFrames.getColumns();
        List<DataFrame> dataFrameList = new ArrayList<>();
        for (List<Object> objectList:valuesDataFrames) {
            DataFrame dataFrame = new DataFrame();
            for (int i=0; i<columns1.size(); i++) {
                if (columns1.get(i).equals("time")) dataFrame.setTimeStamp(objectList.get(i).toString());
                if (columns1.get(i).equals("id")) dataFrame.setId(objectList.get(i).toString());
                if (columns1.get(i).equals("data")) dataFrame.setData(objectList.get(i).toString());
                if (columns1.get(i).equals("colors")) dataFrame.setStringColors(objectList.get(i).toString());
            //    if (columns1.get(i).equals("matrix")) dataFrame.setStringMatrix(objectList.get(i).toString());
                if (columns1.get(i).equals("svg")) {
                    String svg = objectList.get(i).toString();

                    dataFrame.setSvgImage(objectList.get(i).toString());
                }

            }
            dataFrameList.add(dataFrame);
        }
        return  dataFrameList;
    }

    public String getSvgByDataFrameId(String sensorName, String dataFrameId){
        Query queryDataFrames = new Query("SELECT svg FROM sensor_"+sensorName+"WHERE id='"+dataFrameId+"'", databaseSeriesProcessed);
        QueryResult queryDataFramesResult = influxDB.query(queryDataFrames);
        List<Result> dataFramesResults = queryDataFramesResult.getResults();
        Series seriesDataFrames = dataFramesResults.get(0).getSeries().get(0);
        List<List<Object>> valuesDataFrames = seriesDataFrames.getValues();
        List<Object> objectList=valuesDataFrames.get(0);
        String svg = objectList.get(0).toString();
        System.out.println(svg);
        return  svg;
    }

    //not used any more, substrituted by aws iot lambda function
    public void sendProcessedDataFrame(DataFrame dataFrame) {
        Point point1 = Point.measurement("sensor_"+dataFrame.getSensorId())
                .time(Long.parseLong(dataFrame.getTimeStamp()), TimeUnit.NANOSECONDS)
                .addField("id",dataFrame.getId())
                .addField("data",dataFrame.getData())
                .addField("matrix", dataFrame.getStringMatrix())
                .addField("colors",dataFrame.getStringColors())
                .addField("svg",dataFrame.getSvgImage())
                .build();
        influxDB.write(databaseSeriesProcessed, "autogen", point1);
    }

}
