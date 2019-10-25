package io.rbajek.rasa.sdk.dto.event;

import java.sql.Timestamp;

public class StoryExported extends AbstractEvent {

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public StoryExported() {
        this(null);
    }

    public StoryExported(Timestamp timestamp) {
        super("export", timestamp);
    }
}
