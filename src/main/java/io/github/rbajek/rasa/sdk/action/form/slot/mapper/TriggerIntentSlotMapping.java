package io.github.rbajek.rasa.sdk.action.form.slot.mapper;

import lombok.ToString;

@ToString(callSuper = true)
public class TriggerIntentSlotMapping<T> extends IntentSlotMapping<T> {

    public TriggerIntentSlotMapping() {
        super(SlotMappingType.TRIGGER_INTENT);
    }

    public static <T> Builder<T> builder() {
        return new Builder(new TriggerIntentSlotMapping());
    }
}
