package io.github.rbajek.rasa.sdk.dto.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@EqualsAndHashCode
public abstract class AbstractEvent {
    protected final String event;
    protected final Long timestamp;

    public AbstractEvent(String event, Timestamp timestamp) {
        this.event = event;
        this.timestamp = timestamp != null ? timestamp.getTime() : null;
    }
}
