package io.github.rbajek.rasa.sdk.dto.event;

import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class FormValidation extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private Object validate;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public FormValidation() {
        this(null);
    }

    public FormValidation(Timestamp timestamp) {
        super("form_validation", timestamp);
    }
}
