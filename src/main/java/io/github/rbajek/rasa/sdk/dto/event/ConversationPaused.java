package io.github.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class ConversationPaused extends AbstractEvent {

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public ConversationPaused() {
        this(null);
    }

    public ConversationPaused(Timestamp timestamp) {
        super("pause", timestamp);
    }
}
