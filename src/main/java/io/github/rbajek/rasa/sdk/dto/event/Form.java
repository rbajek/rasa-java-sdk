package io.github.rbajek.rasa.sdk.dto.event;

import io.github.rbajek.rasa.sdk.util.StringUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.beans.Transient;
import java.sql.Timestamp;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString
public class Form extends AbstractEvent {

    //-----------------------------------------------
    // Fields
    //-----------------------------------------------

    private final String name;

    //-----------------------------------------------
    // Constructors
    //-----------------------------------------------

    public Form(String name) {
        this(name, null);
    }

    public Form(String name, Timestamp timestamp) {
        super("form", timestamp);
        this.name = name;
    }

    //-----------------------------------------------
    // Class methods
    //-----------------------------------------------

    @Transient
    public boolean isActive() {
        return StringUtils.isNotNullOrEmpty(this.name);
    }

    @Transient
    public boolean isNotActive() {
        return !isActive();
    }
}
