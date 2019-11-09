package io.github.rbajek.rasa.sdk.dto;

import io.github.rbajek.rasa.sdk.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter @Setter @ToString
public class Tracker {

    //=================================================
    // Class fields
    //=================================================

    @JsonProperty("conversation_id")
    private String conversationId;

    private Map<String, Object> slots;

    @JsonProperty("sender_id")
    private String senderId;

    @JsonProperty("latest_message")
    private Message latestMessage;

    @JsonProperty("followup_action")
    private String followupAction;

    private Boolean paused;

    private List<Event> events;

    @JsonProperty("latest_input_channel")
    private String latestInputChannel;

    @JsonProperty("latest_action_name")
    private String latestActionName;

    @JsonProperty("active_form")
    private Form activeForm;

    //=================================================
    // Class methods
    //=================================================

    public boolean hasActiveForm() {
        return this.activeForm != null && StringUtils.isNotNullOrEmpty(this.activeForm.name);
    }

    /**
     * Get entity values found for the passed entity name in latest msg.
     *
     * @param entityName an entity name
     * @return value of given entity name
     */
    public List<String> getLatestEntityValues(String entityName) {
        if(latestMessage.entities == null) {
            return Collections.emptyList();
        }

        return latestMessage.entities.stream()
                .filter(entity -> entityName.equals(entity.getEntity()))
                .map(Entity::getValue)
                .collect(Collectors.toList());
    }

    public void addSlot(String slotName, Object value) {
        if(this.slots == null) {
            this.slots = new HashMap<>();
        }
        this.slots.put(slotName, value);
    }

    public Map<String, Object> getSlots() {
        return this.slots != null ? this.slots : Collections.emptyMap();
    }

    public boolean hasSlots() {
        return this.slots != null ? this.slots.isEmpty() == false : false;
    }

    public Object getSlotValue(String slotName) {
        return getSlotValue(slotName, Object.class);
    }

    public <T> T getSlotValue(String slotName, Class<T> type) {
        if(this.slots != null && this.slots.containsKey(slotName)) {
            return type.cast(this.slots.get(slotName));
        }
        return null;
    }

    public boolean hasSlotValue(String slotName) {
        if(this.slots != null && this.slots.containsKey(slotName)) {
            return this.slots.get(slotName) != null;
        }
        return false;
    }

    //=================================================
    // Inner Types
    //=================================================

    @Getter @Setter @ToString
    public static class Message {

        private List<Entity> entities;

        private Intent intent;

        @JsonProperty("intent_ranking")
        private List<Intent> intentRanking;

        private String text;
    }

    @Getter @Setter @ToString
    public static class Event {
        private String event;
        private Long timestamp;
    }

    @Getter @Setter @ToString
    public static class Form {
        private String name;
        private Boolean validate;
        private Boolean rejected;
        @JsonProperty("trigger_message")
        private Message triggerMessage;

        public boolean shouldValidate(Boolean defaultValue) {
            return validate != null ? validate : defaultValue;
        }
    }

    @Getter @Setter @ToString
    public static class Intent {
        private Double confidence;
        private String name;
    }

    @Getter @Setter @ToString
    public static class Entity {
        private Integer start;
        private Integer end;
        private String value;
        private String entity;
        private Double confidence;
    }
}
