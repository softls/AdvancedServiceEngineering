package at.ac.tuwien.infosys.controllers;


import at.ac.tuwien.infosys.ModelAttributes;
import at.ac.tuwien.infosys.SensorApplication;
import at.ac.tuwien.infosys.Utils;
import at.ac.tuwien.infosys.entities.DataStore;
import at.ac.tuwien.infosys.entities.SensorDescription;
import at.ac.tuwien.infosys.exceptions.AccessException;
import at.ac.tuwien.infosys.session.SessionProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import static at.ac.tuwien.infosys.controllers.MainNavController.SENSORS_URL;
import static at.ac.tuwien.infosys.controllers.MainNavController.redirectWithError;


/**
 * Created by lenaskarlat on 4/10/17.
 */
@Controller
@RequestMapping(SENSORS_URL)
public class SensorApplicationController {
    private static final String ID = "/{id}";
    private static final String DATA_FRAME_ID = "/{dataFrameId}";
    private final SessionProxy sessionProxy;

    @Autowired
    public SensorApplicationController(SessionProxy sessionProxy) {
        this.sessionProxy = sessionProxy;
    }

    @RequestMapping(value = ID, method = RequestMethod.GET)
    public String showDataFrames(@PathVariable String id, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws Exception {
        ModelAttributes.initForSensors(model);
        return get_start(id, model, redirectAttributes);
    }

    @RequestMapping(value = ID+"/show/"+DATA_FRAME_ID, method = RequestMethod.GET)
    public String showDataFrame(@PathVariable String id, @PathVariable String dataFrameId, HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws Exception {
        sessionProxy.getInfluxSessionBean().getSvgByDataFrameId(id,dataFrameId);
        return "sensor";
    }

    private String get_start(String id, Model model, RedirectAttributes redirectAttributes) {
        try {
            initSensor(model, id);
            return "sensor";
        } catch (AccessException ex) {
            return redirectWithError(redirectAttributes, MainNavController.INDEX_URL, "Error while fetching business object", ex);
        }
    }

    protected void initSensor(Model model, String id) throws AccessException {
        sessionProxy.getSensorSessionBean().init(sessionProxy.getInfluxSessionBean().getSensorByName(id), model);
    }


    protected SensorDescription getWorkingSensor() {
        return sessionProxy.getSensorSessionBean().getWorkingSensor();
    }


}
