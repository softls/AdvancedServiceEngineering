package at.ac.tuwien.infosys.session;

import at.ac.tuwien.infosys.entities.DataStore;
import at.ac.tuwien.infosys.entities.SensorDescription;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
/**
 * Created by lenaskarlat on 5/31/17.
 */


@Component
@Scope(value = "session")
public class SensorSessionBean {

    private SensorDescription workingSensor;


    public void init(SensorDescription sensorDescription, Model model) {
        workingSensor = sensorDescription;
        saveTo(model);
    }

    public void initContinue(Model model) {
        saveTo(model);
    }

    private void saveTo(Model model) {
        model.addAttribute("object", workingSensor);
    }

    public SensorDescription getWorkingSensor() {
        return workingSensor;
    }

}
