package io.github.rbajek.rasa.sdk.action.form.slot.mapper;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
public class EntitySlotMapping extends AbstractSlotMapping {

    private final String entity;

    private EntitySlotMapping(String entity) {
        super(SlotMappingType.ENTITY);
        this.entity = entity;
    }

    public static Builder builder(String entity) {
        return new Builder(entity);
    }

    public static class Builder extends AbstractSlotMapping.AbstractBuilder<EntitySlotMapping, Builder> {

        public Builder(String entity) {
            super(new EntitySlotMapping(entity));
        }
    }
}
