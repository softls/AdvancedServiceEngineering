package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;



import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenaskarlat on 6/21/17.
 */
public class LambdaPostToInflux implements RequestHandler<RequestClass, ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass request, Context context) {
        DataFrame dataFrame = analyseMessage(request.dataFrameMessage);
        System.out.println("Data frame created.");
        sendDataFrameToInflux(dataFrame);
        return null;
    }
    private void sendDataFrameToInflux(DataFrame dataFrame) {
        try {
            String url = "http://ec2-54-187-68-156.us-west-2.compute.amazonaws.com:8086/write?db=PositioningDataSeriesProcessed";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String userpass = "superadmin" + ":" + "superadmin";
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
            conn.setRequestProperty ("Authorization", basicAuth);
            StringBuilder payload=new StringBuilder("sensor_"+dataFrame.getSensorId()+",");
            payload.append("id="+dataFrame.getId()+" ");
            payload.append("data=\""+dataFrame.getData()+"\",");
            payload.append("svg=\""+dataFrame.getSvgImage()+"\",");
            payload.append("colors=\""+dataFrame.getStringColors()+"\" ");
            payload.append(dataFrame.getTimeStamp());
            String data = payload.toString();
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(data);
            out.close();
            new InputStreamReader(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //parses message from mqtt into data frame with processed data (colors, svg)
    private static DataFrame analyseMessage(String message){
        String[] measurementArray = message.split(";");
        String sensorName =  measurementArray[0].substring(Math.max(measurementArray[0].length() - 2, 0));;
        System.out.println("Topic: "+sensorName);
        String time = measurementArray[1];
        System.out.println("time="+time);
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
