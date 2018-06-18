package at.ac.tuwien.infosys.access;

import at.ac.tuwien.infosys.session.SessionProxy;
import com.amazonaws.services.iot.client.AWSIotException;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by lenaskarlat on 6/14/17.
 */
@Service
public class AWSSubscribeAccess {

    private final SessionProxy sessionProxy;
    private static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;
    private static String[] topics;
    private static AWSIotMqttClient awsIotMqttClient;
    private static String clientEndpoint;
    private static String clientId;
    private static String certificateFile;
    private static String privateKeyFile;

    @Autowired
    public AWSSubscribeAccess(SessionProxy sessionProxy,
                              @Value("${clientEndpoint}") String clientEndpoint,
                              @Value("${clientId}") String clientId,
                              @Value("${topics}") String topicsString,
                              @Value("${certificateFile}") String certificateFile,
                              @Value("${privateKeyFile}") String privateKeyFile) {
        this.sessionProxy = sessionProxy;
        this.clientEndpoint =clientEndpoint;
        this.clientId=clientId;
        this.topics=topicsString.split(",");
        this.certificateFile=certificateFile;
        this.privateKeyFile=privateKeyFile;
        initClient();
        try {
            awsIotMqttClient.connect();
        } catch (AWSIotException e) {
            e.printStackTrace();
        }
    }

    private static void initClient() {
        if (awsIotMqttClient == null && certificateFile != null && privateKeyFile != null) {
            ConnectionManager.KeyStorePasswordPair pair = ConnectionManager.getKeyStorePasswordPair(certificateFile, privateKeyFile, null);
            awsIotMqttClient=new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }
        if (awsIotMqttClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }
    }

    public void subscribe() {
        for (String topicString : topics) {
            AWSIotTopic topic = new TopicListener(topicString, TestTopicQos, sessionProxy.getInfluxSessionBean().getInfluxAccess());
            try {
                awsIotMqttClient.subscribe(topic,false);
            } catch (AWSIotException e) {
                e.printStackTrace();
            }
        }
    }
}

