package io.rbajek.rasa.sdk;

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
     * @param elements
     */
    public void utterElements(Object[] elements) {
        utterElements(elements, null);
    }

    /**
     * Sends a message with custom elements to the output channel.
     *
     * @param elements
     * @param kwargs
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
     * @param text
     */
    public void utterMessage(String text) {
        utterMessage(text, null);
    }

    /**
     * Send a text to the output channel
     *
     * @param text
     * @param kwargs
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
     * @param text
     * @param buttons
     */
    public void utterButtonMessage(String text, List<Map<String, Object>> buttons) {
        utterButtonMessage(text, buttons, null);
    }

    /**
     * Sends a message with buttons to the output channel.
     *
     * @param text
     * @param buttons
     * @param kwargs
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
     * @param attachment
     */
    public void utterAttachment(String attachment) {
        utterAttachment(attachment, null);
    }

    /**
     * Send a message to the client with attachments.
     *
     * @param attachment
     * @param kwargs
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
     * @param template
     * @param buttons
     */
    public void utterButtonTemplate(String template, List<Map<String, Object>> buttons) {
        utterButtonTemplate(template, buttons, null);
    }

    /**
     * Sends a message template with buttons to the output channel.
     *
     * @param template
     * @param buttons
     * @param kwargs
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
     * @param template
     */
    public void utterTemplate(String template) {
        utterTemplate(template, null);
    }

    /**
     * Send a message to the client based on a template.
     *
     * @param template
     * @param kwargs
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
     * @param jsonMessage
     */
    public void utterCustomJson(String jsonMessage) {
        utterCustomJson(jsonMessage, null);
    }

    /**
     * Sends custom json to the output channel.
     *
     * @param jsonMessage
     * @param kwargs
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
     * @param image
     */
    public void utterImageUrl(String image) {
        utterImageUrl(image, null);
    }

    /**
     * Sends url of image attachment to the output channel.
     *
     * @param image
     * @param kwargs
     */
    public void utterImageUrl(String image, Map<String, Object> kwargs) {
        messages.put(MSG_IMAGE_KEY, image);
        if(kwargs != null) {
            messages.putAll(kwargs);
        }
    }
}
