package io.github.rbajek.rasa.sdk.dto.event;

import io.github.rbajek.rasa.sdk.dto.Tracker;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class UserUttered extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------
    private String text;

    @JsonProperty("parse_data")
    private Tracker.Message parseData;

    @JsonProperty("input_channel")
    private String inputChannel;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public UserUttered() {
        this(null);
    }

    public UserUttered(Timestamp timestamp) {
        super("user", timestamp);
    }

    //-----------------------------------------------
    // Getters/Setters
    //-----------------------------------------------
}
