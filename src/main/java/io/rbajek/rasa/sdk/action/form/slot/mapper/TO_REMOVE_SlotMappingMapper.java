package io.rbajek.rasa.sdk.action.form.slot.mapper;

import io.rbajek.rasa.sdk.exception.RasaException;
import io.rbajek.rasa.sdk.util.CollectionsUtils;

import java.util.List;

public class TO_REMOVE_SlotMappingMapper {

    public static EntitySlotMappingBuilder entityMapper(String entity) {
        return new EntitySlotMappingBuilder(entity);
    }

    public static class EntitySlotMappingBuilder extends AbstractSlotMappingBuilder<EntitySlotMapping.Builder, EntitySlotMappingBuilder> {

        public EntitySlotMappingBuilder(String entity) {
            super(EntitySlotMapping.builder(entity));
        }
    }

    public static class AbstractSlotMappingBuilder<T extends AbstractSlotMapping.AbstractBuilder, B> {
        protected final T slotMappingBuilder;

        public AbstractSlotMappingBuilder(T slotMappingBuilder) {
            this.slotMappingBuilder = slotMappingBuilder;
        }

        public B intent(String intent) {
            slotMappingBuilder.intent(intent);
            return (B) slotMappingBuilder;
        }

        public B noIntent(String noIntent) {
            slotMappingBuilder.notIntent(noIntent);
            return (B) slotMappingBuilder;
        }
    }

    protected EntitySlotMapping fromEntity(String entity, List<String> intent, List<String> notIntent) {
        if(CollectionsUtils.isNotEmpty(intent) && CollectionsUtils.isNotEmpty(notIntent)) {
            throw new RasaException("Providing  both intent '" + intent + "' and notIntent '" + notIntent + "' is not supported");
        }

        return EntitySlotMapping.builder(entity)
                .intent(intent)
                .notIntent(notIntent)
                .build();
    }
}
