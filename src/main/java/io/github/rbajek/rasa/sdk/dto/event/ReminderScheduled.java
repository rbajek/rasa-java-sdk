package io.github.rbajek.rasa.sdk.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
public class ReminderScheduled extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private String action;
    @JsonProperty("date_time")
    private LocalDate date;
    private String name;
    @JsonProperty("kill_on_user_msg")
    private String killOnUserMsg;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public ReminderScheduled() {
        this(null);
    }

    public ReminderScheduled(Timestamp timestamp) {
        super("reminder", timestamp);
    }
}
