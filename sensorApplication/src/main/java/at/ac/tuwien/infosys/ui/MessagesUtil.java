package at.ac.tuwien.infosys.ui;

/**
 * Created by lenaskarlat on 4/10/17.
 */

import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Provides means for showing messages from controller results in the view.
 * Currently, there are two supported message types: Warning and Success.
 */
@SuppressWarnings("unchecked")
public class MessagesUtil {
    /**
     * Adds a message about a form error ({@link ObjectError}), to a {@link RedirectAttributes} object, as a warning.
     *
     * @param redirectAttributes attribute object to hold the message
     * @param objectError        the occurred object error
     */
    public static void add(RedirectAttributes redirectAttributes, ObjectError objectError) {
        addWarning(redirectAttributes, phrase(objectError));
    }

    /**
     * Adds a message about a form error ({@link ObjectError}), to a {@link org.springframework.ui.Model}, as a warning.
     *
     * @param model       the model to add to
     * @param objectError the occurred object error
     */
    public static void add(Model model, ObjectError objectError) {
        addWarning(model, phrase(objectError));
    }

    /**
     * Adds a message about an exception, as a warning.
     *
     * @param redirectAttributes attribute object to hold the message
     * @param message            an optional message describing the process which involved the exception
     * @param throwable          the occurred exception
     */
    public static void add(RedirectAttributes redirectAttributes, String message, Throwable throwable) {
        addWarning(redirectAttributes, phrase(message, throwable));
    }

    private static String phrase(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            FieldError fieldError = (FieldError) objectError;
            return fieldError.getField() + ": " + fieldError.getDefaultMessage();
        } else return objectError.getDefaultMessage();
    }

    private static String phrase(String message, Throwable throwable) {
        if (message == null || message.trim().length() == 0)
            return throwable.getMessage();
        return message + ": " + throwable.getMessage();
    }

    private static void add(Map<String, Object> attributes, String type, String message) {
        String key = createKey(type);
        if (!attributes.containsKey(key))
            attributes.put(key, new ArrayList<String>());
        ((List<String>) attributes.get(key)).add(message);
    }

    private static String createKey(String type) {
        return "messages" + Character.toUpperCase(type.charAt(0)) + type.substring(1);
    }

    /**
     * Adds a simple {@link String} message, to a {@link RedirectAttributes} object, as a warning.
     *
     * @param redirectAttributes attribute object to hold the message
     * @param message            the raw message to add
     */
    public static void addWarning(RedirectAttributes redirectAttributes, String message) {
        add((Map<String, Object>) redirectAttributes.getFlashAttributes(), "warning", message);
    }


    /**
     * Adds a simple {@link String} message, to a {@link RedirectAttributes} object, as a success message.
     *
     * @param redirectAttributes attribute object to hold the message
     * @param message            the raw message to add
     */
    public static void addSuccess(RedirectAttributes redirectAttributes, String message) {
        add((Map<String, Object>) redirectAttributes.getFlashAttributes(), "success", message);
    }

    /**
     * Adds a simple {@link String} message, to a {@link Model} object, as a warning.
     *
     * @param model   the model to add to
     * @param message the raw message to add
     */
    public static void addWarning(Model model, String message) {
        add(model.asMap(), "warning", message);
    }


    /**
     * Adds a simple {@link String} message, to a {@link Model} object, as a success message.
     *
     * @param model   the model to add to
     * @param message the raw message to add
     */
    public static void addSuccess(Model model, String message) {
        add(model.asMap(), "success", message);
    }
}