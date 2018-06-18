package at.ac.tuwien.infosys.access;

import at.ac.tuwien.infosys.Utils;
import at.ac.tuwien.infosys.entities.DataFrame;
import at.ac.tuwien.infosys.session.SessionProxy;
import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by lenaskarlat on 6/14/17.
 */
public class TopicListener extends AWSIotTopic {

    @Autowired
    private final InfluxAccess influxAccess;

    public TopicListener(String topic, AWSIotQos qos,InfluxAccess influxAccess) {
        super(topic, qos);
        this.influxAccess=influxAccess;
    }

    @Override
    public void onMessage(AWSIotMessage message) {
        System.out.println(System.currentTimeMillis() + ": <<< " + message.getStringPayload());

      //the next functionality was substituted by lambda functions, but this writing to influx db works, left here just in case it is needed
      //  DataFrame dataFrame = Utils.analyseMessage(super.getTopic(),message.getStringPayload());
      //  influxAccess.sendProcessedDataFrame(dataFrame);         //send processed dataframe to database

      //  influxAccess.sendDataFrame(message.getStringPayload()); //preserve in db - raw message
    }

}
