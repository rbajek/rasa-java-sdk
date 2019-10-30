package io.github.rbajek.rasa.sdk.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class ActionRequest {

    @JsonProperty("next_action")
    private String nextAction;

    @JsonProperty("sender_id")
    private String senderId;

    private Tracker tracker;

    private Domain domain;

    private String version;
}
