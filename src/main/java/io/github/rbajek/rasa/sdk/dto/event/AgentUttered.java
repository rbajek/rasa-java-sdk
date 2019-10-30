package io.github.rbajek.rasa.sdk.dto.event;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class AgentUttered extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private String text;
    private Object data;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public AgentUttered() {
        this(null);
    }

    public AgentUttered(Timestamp timestamp) {
        super("agent", timestamp);
    }
}
