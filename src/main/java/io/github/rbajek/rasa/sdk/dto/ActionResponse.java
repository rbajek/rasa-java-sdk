package io.github.rbajek.rasa.sdk.dto;

import io.github.rbajek.rasa.sdk.dto.event.AbstractEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter @Setter @ToString
public class ActionResponse {

    private List<AbstractEvent> events;
    private List<Map<String, Object>> responses;
}
