package at.ac.tuwien.infosys.controllers;

import at.ac.tuwien.infosys.ModelAttributes;
import at.ac.tuwien.infosys.entities.DataStore;
import at.ac.tuwien.infosys.session.SessionProxy;
import at.ac.tuwien.infosys.ui.MessagesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Created by lenaskarlat on 4/10/17.
 */
@Controller
public class MainNavController {
    public static final String INDEX_URL="/";
    public static final String SENSORS_URL="/sensors";
    public static final String MAP_URL="/map";
    public static final String VISUAL_URL="/visual";


    @Autowired
    private SessionProxy sessionProxy;

    @RequestMapping(INDEX_URL)
    public String index() {return "index";}


    @RequestMapping(SENSORS_URL)
    public String getSensors(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute(ModelAttributes.OBJECTS, sessionProxy.getInfluxSessionBean().getAllSensors());
            sessionProxy.getAWSSubscribeSessionBean().subscribe();
        } catch (Exception ex) {
            return redirectWithError(redirectAttributes, MainNavController.INDEX_URL, "Unable to get access to the data", ex);
        }
        return "sensors";
    }

    @RequestMapping(MAP_URL)
    public String showMap(Model model, RedirectAttributes redirectAttributes) {
        try {
            model.addAttribute(ModelAttributes.OBJECTS, sessionProxy.getInfluxSessionBean().getAllSensors());
        } catch (Exception ex) {
            return redirectWithError(redirectAttributes, MainNavController.INDEX_URL, "Unable to get access to the data", ex);
        }
        return "map";
    }

    @RequestMapping(VISUAL_URL)
    public String showVisual(Model model, RedirectAttributes redirectAttributes) {
        try {
        } catch (Exception ex) {
            return redirectWithError(redirectAttributes, MainNavController.INDEX_URL, "Unable to get access to the data", ex);
        }
        return "visual";
    }


    /**
     * Utility function, redirecting to a page with an error message.
     *
     * @param redirectAttributes the attribute object for writing the message to
     * @param url                the URL to redirect to
     * @param message            the message to show
     * @param throwable          the throwable causing the message
     */
    public static String redirectWithError(RedirectAttributes redirectAttributes, String url, String message, Throwable throwable) {
        MessagesUtil.add(redirectAttributes, message, throwable);
        return "redirect:" + url;
    }
}
