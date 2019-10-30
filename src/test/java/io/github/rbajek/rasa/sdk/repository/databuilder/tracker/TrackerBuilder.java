package io.github.rbajek.rasa.sdk.repository.databuilder.tracker;

import io.github.rbajek.rasa.sdk.dto.Tracker;

public class TrackerBuilder {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Tracker tracker;

        public Builder() {
            this.tracker = new Tracker();
        }

        public Builder senderId(String value) {
            this.tracker.setSenderId(value);
            return this;
        }

        public Builder addSlot(String slotName, Object value) {
            this.tracker.addSlot(slotName, value);
            return this;
        }

        public Builder latestMessage(Tracker.Message value) {
            this.tracker.setLatestMessage(value);
            return this;
        }

        public Builder followupAction(String value) {
            this.tracker.setFollowupAction(value);
            return this;
        }

        public Builder paused(boolean value) {
            this.tracker.setPaused(value);
            return this;
        }

        public Builder latestActionName(String value) {
            this.tracker.setLatestActionName(value);
            return this;
        }

        public Builder activeForm(Tracker.Form value) {
            this.tracker.setActiveForm(value);
            return this;
        }

        public Tracker build() {
            return this.tracker;
        }
    }
}
