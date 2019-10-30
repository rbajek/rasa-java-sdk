package io.github.rbajek.rasa.sdk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter @Setter @ToString
public class Domain {

    private Config config;
    private List<Map<String, Intent>> intents;
    private List<String> entities;
    private Map<String, Slot> slots;
    private Map<String, List<Template>> templates;
    private List<String> actions;

    @Getter @Setter @ToString
    public static class Intent {
        @JsonProperty("use_entities")
        private Object use_entities;
    }

    @Getter @Setter @ToString
    public static class Slot {
        @JsonProperty("auto_fill")
        private Boolean autoFill;

        @JsonProperty("initial_value")
        private String initialValue;

        private String type;

        private List<String> values;
    }

    @Getter @Setter @ToString
    public static class Template {
        private String image;
        private String text;
    }

    @Getter @Setter @ToString
    public static class Config {
        @JsonProperty("store_entities_as_slots")
        private Boolean storeEntitiesAsSlots;
    }
}
