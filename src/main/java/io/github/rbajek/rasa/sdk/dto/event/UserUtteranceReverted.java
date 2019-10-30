package io.github.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class UserUtteranceReverted extends AbstractEvent {

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public UserUtteranceReverted() {
        this(null);
    }

    public UserUtteranceReverted(Timestamp timestamp) {
        super("rewind", timestamp);
    }
}
