/*
 * Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

import com.amazonaws.services.iot.client.*;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import connection.CommandArguments;
import connection.ConnectionManager;
import connection.Thing;
import org.json.JSONObject;


import java.io.IOException;
import java.util.*;

/**
 * Created by lenaskarlat on 6/13/17.
 */
public class PublishController {
    private static String topic="";
    private static final AWSIotQos TestTopicQos = AWSIotQos.QOS0;
    private static String sensorName="";
    private static String thingName="";

    private static AWSIotMqttClient awsIotClient;


    public static void setClient(AWSIotMqttClient client) {
        awsIotClient = client;
    }

    public static class NonBlockingPublisher implements Runnable {
        private final AWSIotMqttClient awsIotClient;
        private List<String> paths;
        private Iterator i;

        public NonBlockingPublisher(AWSIotMqttClient awsIotClient) {
            this.awsIotClient = awsIotClient;
            this.paths = Utils.getFilePathsInFolder(sensorName);
            this.i = paths.iterator();

        }

        @Override
        public void run() {
            while (true) {
                if (!i.hasNext()) {
                    i = paths.iterator();
                }
                if (i.hasNext()) {
                    RawDataFrame rdf = new RawDataFrame();
                    String fileName = i.next().toString();
                    try {
                        rdf.readSingleFrame(fileName);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println(fileName);
                    String time = Objects.toString(System.currentTimeMillis(),null);

                  //  String time = Objects.toString(rdf.time, null);
                    System.out.println(time);
                    String data = Arrays.toString(rdf.data);

                    StringBuilder payload=new StringBuilder(sensorName+";");
                    payload.append(time+"000000;");
                    payload.append(data);
                    JSONObject obj = new JSONObject();
                    obj.put("dataFrameMessage", payload.toString());
                    AWSIotMessage message = new NonBlockingPublishListener(topic, TestTopicQos, obj.toString());

                    try {
                        awsIotClient.publish(message);

                    } catch (AWSIotException e) {
                        System.out.println(System.currentTimeMillis() + ": publish failed");
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    System.out.println(System.currentTimeMillis() + ": NonBlockingPublisher was interrupted");
                    return;
                }
            }
        }
    }

    private static void initClient(CommandArguments arguments) {

        String clientEndpoint = arguments.getNotNull("clientEndpoint", ConnectionManager.getConfig("clientEndpoint"));
        String clientId = arguments.getNotNull("clientId", ConnectionManager.getConfig("clientId"));
        thingName = arguments.getNotNull("thingName",ConnectionManager.getConfig("thingName"));
        sensorName = clientId;
        topic = arguments.getNotNull("topic", ConnectionManager.getConfig("topic"));

        String certificateFile = arguments.get("certificateFile", ConnectionManager.getConfig("certificateFile"));
        String privateKeyFile = arguments.get("privateKeyFile", ConnectionManager.getConfig("privateKeyFile"));
        if (awsIotClient == null && certificateFile != null && privateKeyFile != null) {
            String algorithm = arguments.get("keyAlgorithm", ConnectionManager.getConfig("keyAlgorithm"));
            ConnectionManager.KeyStorePasswordPair pair = ConnectionManager.getKeyStorePasswordPair(certificateFile, privateKeyFile, algorithm);
            awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
        }
        if (awsIotClient == null) {
            String awsAccessKeyId = arguments.get("awsAccessKeyId", ConnectionManager.getConfig("awsAccessKeyId"));
            String awsSecretAccessKey = arguments.get("awsSecretAccessKey", ConnectionManager.getConfig("awsSecretAccessKey"));
            String sessionToken = arguments.get("sessionToken", ConnectionManager.getConfig("sessionToken"));

            if (awsAccessKeyId != null && awsSecretAccessKey != null) {
                awsIotClient = new AWSIotMqttClient(clientEndpoint, clientId, awsAccessKeyId, awsSecretAccessKey,
                        sessionToken);
            }
        }
        if (awsIotClient == null) {
            throw new IllegalArgumentException("Failed to construct client due to missing certificate or credentials.");
        }
    }

    public static void main(String args[]) throws InterruptedException, AWSIotException, AWSIotTimeoutException {
        CommandArguments arguments = CommandArguments.parse(args);
        initClient(arguments);
        awsIotClient.connect();
        Thread nonBlockingPublishThread = new Thread(new NonBlockingPublisher(awsIotClient));
        nonBlockingPublishThread.start();
        nonBlockingPublishThread.join();
    }

}
