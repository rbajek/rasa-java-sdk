package io.github.rbajek.rasa.sdk.dto.event;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class ReminderCancelled extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private String action;
    private String name;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public ReminderCancelled() {
        this(null);
    }

    public ReminderCancelled(Timestamp timestamp) {
        super("cancel_reminder", timestamp);
    }
}
