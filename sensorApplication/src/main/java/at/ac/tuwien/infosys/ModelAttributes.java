package at.ac.tuwien.infosys;

import at.ac.tuwien.infosys.entities.SensorDescription;
import org.springframework.ui.Model;

import static at.ac.tuwien.infosys.controllers.MainNavController.SENSORS_URL;

/**
 * Created by lenaskarlat on 4/18/17.
 */
public class ModelAttributes {

    public static final String RECEIVED_DATA_TIMESTAMPS_FORM = "receivedDataTimeStampsForm";
    public static final String OBJECT_LIST = "objectList";
    public static final String PATH = "path";
    public static final String OBJECTS = "objects";
    public static final String NAME_SINGULAR = "nameSingular";
    public static final String NAME_PLURAL = "namePlural";

    private ModelAttributes() {
    }

    public static void initForSensors(Model model) {
        //model.addAttribute(ModelAttributes.PATH, SENSORS_URL);
        model.addAttribute(ModelAttributes.NAME_SINGULAR, "Sensor");
        model.addAttribute(ModelAttributes.NAME_PLURAL, "Sensors");
    }


}