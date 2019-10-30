package io.github.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class ActionReverted extends AbstractEvent {

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public ActionReverted() {
        this(null);
    }

    public ActionReverted(Timestamp timestamp) {
        super("undo", timestamp);
    }
}
