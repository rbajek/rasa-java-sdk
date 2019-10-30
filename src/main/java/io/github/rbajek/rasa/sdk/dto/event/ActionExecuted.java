package io.github.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class ActionExecuted extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private String name;
    private String policy;
    private Float confidence;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public ActionExecuted() {
        this(null);
    }

    public ActionExecuted(Timestamp timestamp) {
        super("action", timestamp);
    }
}
