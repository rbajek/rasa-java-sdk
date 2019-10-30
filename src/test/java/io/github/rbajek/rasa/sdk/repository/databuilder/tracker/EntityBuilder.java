package io.github.rbajek.rasa.sdk.repository.databuilder.tracker;


import io.github.rbajek.rasa.sdk.dto.Tracker;

public class EntityBuilder {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Tracker.Entity entity;

        public Builder() {
            this.entity = new Tracker.Entity();
        }

        public Builder start(int value) {
            this.entity.setStart(value);
            return this;
        }

        public Builder end(int value) {
            this.entity.setEnd(value);
            return this;
        }

        public Builder value(String value) {
            this.entity.setValue(value);
            return this;
        }

        public Builder entity(String value) {
            this.entity.setEntity(value);
            return this;
        }

        public Builder confidence(double value) {
            this.entity.setConfidence(value);
            return this;
        }

        public Tracker.Entity build() {
            return entity;
        }
    }
}
