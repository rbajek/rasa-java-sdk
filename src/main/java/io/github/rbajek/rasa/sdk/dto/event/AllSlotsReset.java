package io.github.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class AllSlotsReset extends AbstractEvent {

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public AllSlotsReset() {
        this(null);
    }

    public AllSlotsReset(Timestamp timestamp) {
        super("reset_slots", timestamp);
    }
}
