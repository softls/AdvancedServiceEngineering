package at.ac.tuwien.infosys.session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

/**
 * Created by lenaskarlat on 5/31/17.
 */


public interface SessionProxy {
    SensorSessionBean getSensorSessionBean();

    InfluxSessionBean getInfluxSessionBean();

    AWSSubscribeSessionBean getAWSSubscribeSessionBean();

}

@SuppressWarnings("SpringAutowiredFieldsWarningInspection")
@Service("sessionProxy")
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.INTERFACES)
class SessionProxyImpl implements SessionProxy {
    @Autowired
    private SensorSessionBean sensorSessionBean;

    @Autowired
    private InfluxSessionBean influxSessionBean;

    @Autowired
    private AWSSubscribeSessionBean awsSubscribeSessionBean;



    @Override
    public SensorSessionBean getSensorSessionBean() {
        return sensorSessionBean;
    }

    @Override
    public InfluxSessionBean getInfluxSessionBean() {
        return influxSessionBean;
    }

    @Override
    public AWSSubscribeSessionBean getAWSSubscribeSessionBean() {
        return awsSubscribeSessionBean;
    }
}
