package io.github.rbajek.rasa.sdk.action.form.slot.mapper;

import io.github.rbajek.rasa.sdk.util.CollectionsUtils;
import io.github.rbajek.rasa.sdk.exception.RasaException;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@ToString
public abstract class AbstractSlotMapping {

    //=================================================
    // Class fields
    //=================================================

    protected final SlotMappingType type;
    protected List<String> intent;
    protected List<String> notIntent;

    //=================================================
    // Constructors
    //=================================================

    public AbstractSlotMapping(SlotMappingType type) {
        this.type = type;
    }

    //=================================================
    // Class methods
    //=================================================

    public boolean isEntitySlotMappingType() {
        return SlotMappingType.ENTITY == type;
    }

    public boolean isTriggerIntentSlotMappingType() {
        return SlotMappingType.TRIGGER_INTENT == type;
    }

    //=================================================
    // Builder
    //=================================================

    public abstract static class AbstractBuilder<T extends AbstractSlotMapping, B> {
        protected final T instance;

        public AbstractBuilder(T instance) {
            this.instance = instance;
        }

        public B intent(String intent) {
            if(this.instance.intent == null) {
                this.instance.intent = new ArrayList<>();
            }
            this.instance.intent.add(intent);
            return (B) this;
        }

        public B intent(List<String> intents) {
            if(this.instance.intent == null) {
                this.instance.intent = new ArrayList<>();
            }
            this.instance.intent.addAll(intents);
            return (B) this;
        }

        public B notIntent(String notIntent) {
            if(this.instance.notIntent == null) {
                this.instance.notIntent = new ArrayList<>();
            }
            this.instance.notIntent.add(notIntent);
            return (B) this;
        }

        public B notIntent(List<String> notIntents) {
            if(this.instance.notIntent == null) {
                this.instance.notIntent = new ArrayList<>();
            }
            this.instance.notIntent.addAll(notIntents);
            return (B) this;
        }

        public T build() {
            if(CollectionsUtils.isNotEmpty(this.instance.intent) && CollectionsUtils.isNotEmpty(this.instance.notIntent)) {
                throw new RasaException("Providing  both intent '" + this.instance.intent + "' and notIntent '" + this.instance.notIntent + "' is not supported");
            }

            if(this.instance.intent == null) {
                this.instance.intent = Collections.emptyList();
            }
            if(this.instance.notIntent == null) {
                this.instance.notIntent = Collections.emptyList();
            }
            return this.instance;
        }
    }

    //=================================================
    // Inner Types
    //=================================================

    @Getter
    public enum SlotMappingType {
        ENTITY ("from_entity"),
        INTENT ("from_intent"),
        TEXT ("from_text"),
        TRIGGER_INTENT ("from_trigger_intent");

        private final String value;

        SlotMappingType(String value) {
            this.value = value;
        }
    }
}
