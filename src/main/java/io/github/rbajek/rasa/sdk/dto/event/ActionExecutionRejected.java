package io.github.rbajek.rasa.sdk.dto.event;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ActionExecutionRejected extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private String name;
    private String policy;
    private Float confidence;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public ActionExecutionRejected() {
        this(null);
    }

    public ActionExecutionRejected(Timestamp timestamp) {
        super("form_validation", timestamp);
    }
}
