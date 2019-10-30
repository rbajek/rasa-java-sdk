package io.github.rbajek.rasa.sdk.dto.event;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class FollowupAction extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private String name;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public FollowupAction() {
        this(null);
    }

    public FollowupAction(Timestamp timestamp) {
        super("followup", timestamp);
    }
}
