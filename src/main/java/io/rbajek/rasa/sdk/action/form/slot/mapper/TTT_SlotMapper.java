package io.rbajek.rasa.sdk.action.form.slot.mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TTT_SlotMapper {

    private List<AbstractSlotMapping> slotMappings = new ArrayList<>();

    public void collect(AbstractSlotMapping... slotMapper) {
        slotMappings.addAll(Arrays.asList(slotMapper));
    }

//    public static EntitySlotMapping.Builder entityMapperBuilder(String entity) {
//        return EntitySlotMapping.builder(entity);
//    }
}
