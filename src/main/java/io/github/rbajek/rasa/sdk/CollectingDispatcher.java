package io.github.rbajek.rasa.sdk;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Send messages back to user
 *
 * @author Rafał Bajek
 */
@Getter
public class CollectingDispatcher {

    private static final String MSG_TEXT_KEY = "text";
    private static final String MSG_ELEMENTS_KEY = "elements";
    private static final String MSG_BUTTONS_KEY = "buttons";
    private static final String MSG_ATTACHMENT_KEY = "attachment";
    private static final String MSG_TEMPLATE_KEY = "template";
    private static final String MSG_CUSTOM_KEY = "custom";
    private static final String MSG_IMAGE_KEY = "image";

    private Map<String, Object> messages = new HashMap<>();

    /**
     * Sends a message with custom elements to the output channel.
     *
     * @param elements array of custom elements
     */
    public void utterElements(Object[] elements) {
        utterElements(elements, null);
    }

    /**
     * Sends a message with custom elements to the output channel.
     *
     * @param elements array of custom elements
     * @param kwargs map of custom elements (optional)
     */
    public void utterElements(Object[] elements, Map<String, Object> kwargs) {
        messages.put(MSG_TEXT_KEY, null);
        messages.put(MSG_ELEMENTS_KEY, elements);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }

    /**
     * Send a text to the output channel
     *
     * @param text a text message
     */
    public void utterMessage(String text) {
        utterMessage(text, null);
    }

    /**
     * Send a text to the output channel
     *
     * @param text a text message
     * @param kwargs map with text messages (optional)
     */
    public void utterMessage(String text, Map<String, Object> kwargs) {
        messages.put(MSG_TEXT_KEY, text);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }

    /**
     * Sends a message with buttons to the output channel.
     *
     * @param text a text message
     * @param buttons list of map of buttons
     */
    public void utterButtonMessage(String text, List<Map<String, Object>> buttons) {
        utterButtonMessage(text, buttons, null);
    }

    /**
     * Sends a message with buttons to the output channel.
     *
     * @param text a text message
     * @param buttons list of map of buttons
     * @param kwargs map of utter button messages (optional)
     */
    public void utterButtonMessage(String text, List<Map<String, Object>> buttons, Map<String, Object> kwargs) {
        messages.put(MSG_TEXT_KEY, text);
        messages.put(MSG_BUTTONS_KEY, buttons);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }

    /**
     * Send a message to the client with attachments.
     *
     * @param attachment a attachment
     */
    public void utterAttachment(String attachment) {
        utterAttachment(attachment, null);
    }

    /**
     * Send a message to the client with attachments.
     *
     * @param attachment a attachment
     * @param kwargs map of attachments (optional)
     */
    public void utterAttachment(String attachment, Map<String, Object> kwargs) {
        messages.put(MSG_TEXT_KEY, null);
        messages.put(MSG_ATTACHMENT_KEY, attachment);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }

    /**
     * Sends a message template with buttons to the output channel.
     *
     * @param template a template
     * @param buttons list of map of buttons
     */
    public void utterButtonTemplate(String template, List<Map<String, Object>> buttons) {
        utterButtonTemplate(template, buttons, null);
    }

    /**
     * Sends a message template with buttons to the output channel.
     *
     * @param template a template
     * @param buttons list of map of buttons
     * @param kwargs map of templates with buttons (optional)
     */
    public void utterButtonTemplate(String template, List<Map<String, Object>> buttons, Map<String, Object> kwargs) {
        messages.put(MSG_TEMPLATE_KEY, template);
        messages.put(MSG_BUTTONS_KEY, buttons);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }

    /**
     * Send a message to the client based on a template.
     *
     * @param template a template
     */
    public void utterTemplate(String template) {
        utterTemplate(template, null);
    }

    /**
     * Send a message to the client based on a template.
     *
     * @param template a template
     * @param kwargs map of templates (optional)
     */
    public void utterTemplate(String template, Map<String, Object> kwargs) {
        messages.put(MSG_TEMPLATE_KEY, template);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }

    /**
     * Sends custom json to the output channel.
     *
     * @param jsonMessage a JSON message
     */
    public void utterCustomJson(String jsonMessage) {
        utterCustomJson(jsonMessage, null);
    }

    /**
     * Sends custom json to the output channel.
     *
     * @param jsonMessage a JSON message
     * @param kwargs map of JSON messages (optional)
     */
    public void utterCustomJson(String jsonMessage, Map<String, Object> kwargs) {
        messages.put(MSG_CUSTOM_KEY, jsonMessage);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }

    /**
     * Sends url of image attachment to the output channel.
     *
     * @param image an URL of image
     */
    public void utterImageUrl(String image) {
        utterImageUrl(image, null);
    }

    /**
     * Sends url of image attachment to the output channel.
     *
     * @param image an URL of image
     * @param kwargs map to an URLs of images (optional)
     */
    public void utterImageUrl(String image, Map<String, Object> kwargs) {
        messages.put(MSG_IMAGE_KEY, image);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }
}
