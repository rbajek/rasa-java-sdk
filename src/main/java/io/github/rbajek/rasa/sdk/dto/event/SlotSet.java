package io.github.rbajek.rasa.sdk.dto.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class SlotSet extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private final String name;
    private final Object value;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public SlotSet(String name, Object value) {
        this(name, value, null);
    }

    public SlotSet(String name, Object value, Timestamp timestamp) {
        super("slot", timestamp);
        this.name = name;
        this.value = value;
    }

    //-----------------------------------------------
    // Getters/Setters
    //-----------------------------------------------
}
