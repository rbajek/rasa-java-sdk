package io.github.rbajek.rasa.sdk.dto.event;

import lombok.Getter;

import java.sql.Timestamp;

/**
 * Not used for now
 */
@Getter
public class BotUttered extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private String text;
    private Object data;
    private Metadata metadata;

   //-----------------------------------------------
   // Constructors
   //-----------------------------------------------

    public BotUttered() {
        this(null);
    }

    public BotUttered(Timestamp timestamp) {
        super("bot", timestamp);
    }

    //-----------------------------------------------
    // Inner types
    //-----------------------------------------------

    public static class Metadata {

    }

    //-----------------------------------------------
    // Getters/Setters
    //-----------------------------------------------
}
