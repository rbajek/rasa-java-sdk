package io.github.rbajek.rasa.sdk.action.form.slot.mapper;

import lombok.ToString;

@ToString(callSuper = true)
public class TextSlotMapping extends AbstractSlotMapping {

    public TextSlotMapping() {
        super(SlotMappingType.TEXT);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends AbstractSlotMapping.AbstractBuilder<TextSlotMapping, Builder> {

        public Builder() {
            super(new TextSlotMapping());
        }
    }
}
