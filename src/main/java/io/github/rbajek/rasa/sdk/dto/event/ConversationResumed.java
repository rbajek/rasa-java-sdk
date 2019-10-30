package io.github.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class ConversationResumed extends AbstractEvent {

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public ConversationResumed() {
        this(null);
    }

    public ConversationResumed(Timestamp timestamp) {
        super("resume", timestamp);
    }
}
