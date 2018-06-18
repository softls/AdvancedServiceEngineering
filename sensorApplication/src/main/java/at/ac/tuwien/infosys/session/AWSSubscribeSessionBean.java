package at.ac.tuwien.infosys.session;

import at.ac.tuwien.infosys.access.AWSSubscribeAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by lenaskarlat on 6/16/17.
 */
@Component
@Scope(value="application")
public class AWSSubscribeSessionBean {

    private final AWSSubscribeAccess awsSubscribeAccess;

    @Autowired
    public AWSSubscribeSessionBean(AWSSubscribeAccess awsSubscribeAccess) {
        this.awsSubscribeAccess=awsSubscribeAccess;
    }


    public void subscribe(){
        this.awsSubscribeAccess.subscribe();
    }


}
