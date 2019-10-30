package io.github.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class Restarted extends AbstractEvent {

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public Restarted() {
        this(null);
    }

    public Restarted(Timestamp timestamp) {
        super("restart", timestamp);
    }
}
