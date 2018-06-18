package at.ac.tuwien.infosys;

import at.ac.tuwien.infosys.entities.DataFrame;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenaskarlat on 4/19/17.
 */
public class Utils {

    public static String transformLongToDate(String longTimeStamp) {
        long time = Long.parseLong(longTimeStamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z");
        return sdf.format(time);
    }

    //parses message from mqtt into data frame with processed data (colors, svg)
    public static DataFrame analyseMessage(String topic,String message){
        String sensorName = topic.substring(Math.max(topic.length() - 2, 0));
        String[] measurementArray = message.split(";");
        String time = measurementArray[1];
        String data = measurementArray[2];
        try {
            DataFrame dataFrame = new DataFrame(sensorName,time,data);
            return dataFrame;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}