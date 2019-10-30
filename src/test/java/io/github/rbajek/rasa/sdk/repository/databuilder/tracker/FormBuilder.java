package io.github.rbajek.rasa.sdk.repository.databuilder.tracker;

import io.github.rbajek.rasa.sdk.dto.Tracker;

public class FormBuilder {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Tracker.Form form;

        public Builder() {
            this.form = new Tracker.Form();
        }


        public Builder name(String value) {
            this.form.setName(value);
            return this;
        }

        public Builder validate(boolean value) {
            this.form.setValidate(value);
            return this;
        }

        public Builder rejected(boolean value) {
            this.form.setRejected(value);
            return this;
        }

        public Builder triggerMessage(Tracker.Message value) {
            this.form.setTriggerMessage(value);
            return this;
        }

        public Tracker.Form build() {
            return this.form;
        }
    }
}
