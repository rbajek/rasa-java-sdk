package io.github.rbajek.rasa.sdk.action.form.slot.mapper;

import io.github.rbajek.rasa.sdk.exception.RasaException;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class IntentSlotMapping<T> extends AbstractSlotMapping {

    protected T value;

    public IntentSlotMapping() {
        this(SlotMappingType.INTENT);
    }

    public IntentSlotMapping(SlotMappingType type) {
        super(type);
        if(SlotMappingType.INTENT != type && SlotMappingType.TRIGGER_INTENT != type) {
            throw new RasaException("Slot mapping type should be one of the: " + SlotMappingType.INTENT.getValue() + " or " + SlotMappingType.TRIGGER_INTENT + " but is: " + type);
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder(new IntentSlotMapping());
    }

    public static class Builder<T> extends AbstractSlotMapping.AbstractBuilder<IntentSlotMapping<T>, Builder<T>> {

        public Builder(IntentSlotMapping intentSlotMapping) {
            super(intentSlotMapping);
        }

        public Builder value(T value) {
            this.instance.value = value;
            return this;
        }
    }
}
