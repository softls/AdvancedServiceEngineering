package lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by lenaskarlat on 6/21/17.
 */
public class LambdaPostRawToInflux implements RequestHandler<RequestClass, ResponseClass> {

    @Override
    public ResponseClass handleRequest(RequestClass request, Context context) {
        String rawDataFrame= analyseMessage(request.dataFrameMessage);
        System.out.println("Data frame received.");
        try{
            String url = "http://ec2-54-187-68-156.us-west-2.compute.amazonaws.com:8086/write?db=PositioningDataSeries";
            URL obj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            String userpass = "superadmin" + ":" + "superadmin";
            String basicAuth = "Basic " + javax.xml.bind.DatatypeConverter.printBase64Binary(userpass.getBytes("UTF-8"));
            conn.setRequestProperty ("Authorization", basicAuth);
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            out.write(rawDataFrame);
            out.close();
            new InputStreamReader(conn.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String analyseMessage(String dataFrameMessage) {

        String[] measurementArray = dataFrameMessage.split(";");
        Long timeConverted = Long.parseLong(measurementArray[1]);
        StringBuilder payload=new StringBuilder(measurementArray[0]+" ");
        payload.append("data=\""+measurementArray[2]+"\" ");
        payload.append(timeConverted);
        return payload.toString();
    }

}
