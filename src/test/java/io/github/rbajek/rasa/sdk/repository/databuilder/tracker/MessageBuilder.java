package io.github.rbajek.rasa.sdk.repository.databuilder.tracker;

import io.github.rbajek.rasa.sdk.dto.Tracker;

import java.util.ArrayList;

public class MessageBuilder {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Tracker.Message latestMessage;

        public Builder() {
            this.latestMessage = new Tracker.Message();
        }

        public Builder addEntity(Tracker.Entity entity) {
            if(this.latestMessage.getEntities() == null) {
                this.latestMessage.setEntities(new ArrayList<>());
            }
            this.latestMessage.getEntities().add(entity);
            return this;
        }

        public Builder intent(String name) {
            return intent(name, null);
        }

        public Builder intent(String name, Double confidence) {
            Tracker.Intent intent = new Tracker.Intent();
            intent.setName(name);
            intent.setConfidence(confidence);
            this.latestMessage.setIntent(intent);
            return this;
        }

        public Builder text(String text) {
            this.latestMessage.setText(text);
            return this;
        }

        public Tracker.Message build() {
            return this.latestMessage;
        }
    }
}
