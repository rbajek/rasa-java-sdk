package io.github.rbajek.rasa.sdk.repository.databuilder.tracker;

import io.github.rbajek.rasa.sdk.dto.Tracker;

public class IntentBuilder {

    public static class Builder {
        private final Tracker.Intent intent;

        public Builder(Tracker.Intent intent) {
            this.intent = intent;
        }

        public Builder confidence(double value) {
            this.intent.setConfidence(value);
            return this;
        }

        public Builder name(String value) {
            this.intent.setName(value);
            return this;
        }

        public Tracker.Intent build() {
            return this.intent;
        }
    }
}
