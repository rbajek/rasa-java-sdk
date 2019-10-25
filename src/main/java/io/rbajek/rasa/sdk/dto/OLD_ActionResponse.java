package io.rbajek.rasa.sdk.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter @ToString
public class OLD_ActionResponse {

    private List<Event> events;
    private List<Response> responses;

    public void addEvent(Event event) {
        if(this.events == null) {
            this.events = new ArrayList<>();
        }
        this.events.add(event);
    }

    public void addResponse(Response response) {
        if(this.responses == null) {
            this.responses = new ArrayList<>();
        }
        this.responses.add(response);
    }

    @Getter @Setter @ToString @Builder
    public static class Event {
        private String event;
        private Long timestamp;

        // this is only for slots
        private String name;
        private String value;
    }

    @Getter @Setter @ToString @Builder
    public static class Response {
        private String text;
        //private Map<String, String> fields;
        private String template;
    }
}
