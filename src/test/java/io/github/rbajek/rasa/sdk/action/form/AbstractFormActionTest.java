package io.github.rbajek.rasa.sdk.action.form;

import io.github.rbajek.rasa.sdk.CollectingDispatcher;
import io.github.rbajek.rasa.sdk.action.form.slot.mapper.*;
import io.github.rbajek.rasa.sdk.dto.Domain;
import io.github.rbajek.rasa.sdk.dto.Tracker;
import io.github.rbajek.rasa.sdk.dto.event.AbstractEvent;
import io.github.rbajek.rasa.sdk.dto.event.Form;
import io.github.rbajek.rasa.sdk.dto.event.SlotSet;
import io.github.rbajek.rasa.sdk.exception.ActionExecutionRejectionException;
import io.github.rbajek.rasa.sdk.repository.databuilder.tracker.EntityBuilder;
import io.github.rbajek.rasa.sdk.repository.databuilder.tracker.FormBuilder;
import io.github.rbajek.rasa.sdk.repository.databuilder.tracker.MessageBuilder;
import io.github.rbajek.rasa.sdk.repository.databuilder.tracker.TrackerBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractFormActionTest {

    /**
     * Test default extraction of a slot value from entity with the same name
     */
    @Test
    void extractRequestedSlotDefault() {
        AbstractFormAction form = new AbstractFormAction("defaultFormName") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder()
                                .entity("some_slot")
                                .value("some_value")
                                .build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = form.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_value");

        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of a slot value from entity with the different name and any intent
     */
    @Test
    void extractRequestedSlotFromEntityNoIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(EntitySlotMapping.builder("some_entity").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder()
                                .entity("some_entity")
                                .value("some_value")
                                .build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_value");

        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of a slot value from entity with the different name and certain intent
     */
    @Test
    void extractRequestedSlotFromEntityWithIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(EntitySlotMapping.builder("some_entity").intent("some_intent").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder()
                                .entity("some_entity")
                                .value("some_value")
                                .build())
                        .intent("some_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_value");

        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of a slot value from entity with the different name and certain intent
     */
    @Test
    void extractRequestedSlotFromEntityWithNotIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(EntitySlotMapping.builder("some_entity").notIntent("some_intent").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder()
                                .entity("some_entity")
                                .value("some_value")
                                .build())
                        .intent("some_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        // check that the value was extracted for correct intent
        assertTrue(slotValues.isEmpty());

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder()
                                .entity("some_entity")
                                .value("some_value")
                                .build())
                        .intent("some_other_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_value");

        // check that the value was not extracted for incorrect intent
        assertEquals(expected, slotValues);

    }

    /**
     * Test extraction of a slot value from certain intent
     */
    @Test
    void extractRequestedSlotFromIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(IntentSlotMapping.builder().intent("some_intent").value("some_value").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .intent("some_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_value");

        // check that the value was not extracted for incorrect intent
        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of a slot value from certain intent
     */
    @Test
    void extractRequestedSlotFromNotIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(IntentSlotMapping.builder().notIntent("some_intent").value("some_value").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .intent("some_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        // check that the value was extracted for correct intent
        assertTrue(slotValues.isEmpty());

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .intent("some_other_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_value");

        // check that the value was not extracted for incorrect intent
        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of a slot value from text with any intent
     */
    @Test
    void extractRequestedSlotFromTextNoIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(TextSlotMapping.builder().build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .text("some_text")
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_text");

        // check that the value was not extracted for incorrect intent
        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of a slot value from text with certain intent
     */
    @Test
    void extractRequestedSlotFromTextWithIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(TextSlotMapping.builder().intent("some_intent").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .text("some_text")
                        .intent("some_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_text");

        // check that the value was not extracted for incorrect intent
        assertEquals(expected, slotValues);

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .text("some_text")
                        .intent("some_other_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);
        // check that the value was not extracted for incorrect intent
        assertTrue(slotValues.isEmpty());
    }

    /**
     * Test extraction of a slot value from text with certain intent
     */
    @Test
    void extractRequestedSlotFromTextWithNotIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return null;
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(TextSlotMapping.builder().notIntent("some_intent").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .text("some_text")
                        .intent("some_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);
        // check that the value was extracted for correct intent
        assertTrue(slotValues.isEmpty());

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .text("some_text")
                        .intent("some_other_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractRequestedSlot(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_text");

        // check that the value was not extracted for incorrect intent
        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of a slot value from trigger intent
     */
    @Test
    void extractTriggerSlots() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(TriggerIntentSlotMapping.builder().intent("trigger_intent").value("some_value").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .latestMessage(MessageBuilder.builder()
                        .intent("trigger_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_slot", "some_value");
        // check that the value was extracted for correct intent
        assertEquals(expected, slotValues);

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .latestMessage(MessageBuilder.builder()
                        .intent("other_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);
        // check that the value was not extracted for incorrect intent
        assertTrue(slotValues.isEmpty());

        //==============================
        // tracker with active form
        //==============================
        tracker = TrackerBuilder.builder()
                .senderId("default")
                .latestMessage(MessageBuilder.builder()
                        .intent("trigger_intent", 1.0)
                        .build())
                .paused(false)
                .activeForm(FormBuilder.builder().name("some_form").validate(true).rejected(false).build())
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);
        // check that the value was not extracted for correct intent
        assertTrue(slotValues.isEmpty());
    }

    /**
     * Test extraction of other not requested slots values from entities with the same names
     */
    @Test
    void extractOtherSlotsNoIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);
        // check that the value was not extracted for correct intent
        assertTrue(slotValues.isEmpty());

        //=====================================

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);
        Map<String, Object> expected = new HashMap<>();
        expected.put("some_other_slot", "some_other_value");
        // check that the value was extracted for non requested slot
        assertEquals(expected, slotValues);

        //=====================================
        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);
        expected = new HashMap<>();
        expected.put("some_other_slot", "some_other_value");
        // check that the value was extracted only for non requested slot
        assertEquals(expected, slotValues);
    }

    /**
     * Test extraction of other not requested slots values from entities with the same names
     */
    @Test
    void extractOtherSlotsWithIntent() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_other_slot", Arrays.asList(EntitySlotMapping.builder("some_other_slot").intent("some_intent").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .intent("some_other_intent", 1.0)
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Map<String, Object> slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);
        // check that the value was extracted for non requested slot
        assertTrue(slotValues.isEmpty());

        //=========================================
        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .intent("some_intent", 1.0)
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.extractOtherSlots(new CollectingDispatcher(), tracker, null);

        Map<String, Object> expected = new HashMap<>();
        expected.put("some_other_slot", "some_other_value");
        // check that the value was extracted only for non requested slot
        assertEquals(expected, slotValues);
    }

    /**
     * Test form validation
     */
    @Test
    void validate() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> events = customFormAction.validate(new CollectingDispatcher(), tracker, null);
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new SlotSet("some_other_slot", "some_other_value"));
        expectedEvents.add(new SlotSet("some_slot", "some_value"));

        // check that validation succeed
        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));

        //==============================================
        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        events = customFormAction.validate(new CollectingDispatcher(), tracker, null);
        expectedEvents = new ArrayList<>();
        expectedEvents.add(new SlotSet("some_other_slot", "some_other_value"));

        // check that validation succeed
        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));

        //==============================================
        Tracker tracker1 = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder().build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        Assertions.assertThrows(ActionExecutionRejectionException.class, () -> {
            customFormAction.validate(new CollectingDispatcher(), tracker1, null);
        },"Failed to extract slot some_slot with action some_form");
    }

    /**
     * Test form validation with custom validator
     */
    @Test
    void setSlotWithinHelper() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {
                //slotValidatorMap.put("some_slot", new SomeSlotValidator());
                slotValidatorMap.put("some_slot", (value, dispatcher, tracker, domain) -> {
                    if("some_value".equals(value)) {
                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("some_slot", "validated_value");
                        resultMap.put("some_other_slot", "other_value");
                        return resultMap;
                    }
                    return Collections.emptyMap();
                });
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> events = customFormAction.validate(new CollectingDispatcher(), tracker, null);
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new SlotSet("some_other_slot", "other_value"));
        expectedEvents.add(new SlotSet("some_slot", "validated_value"));

        // check that validation succeed
        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));
    }

    /**
     * Test form validation with custom validator
     */
    @Test
    void validateExtractedNoRequested() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {
                slotValidatorMap.put("some_slot", (value, dispatcher, tracker, domain) -> {
                    if("some_value".equals(value)) {
                        Map<String, Object> resultMap = new HashMap<>();
                        resultMap.put("some_slot", "validated_value");
                        return resultMap;
                    }
                    return Collections.emptyMap();
                });
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", null)
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> events = customFormAction.validate(new CollectingDispatcher(), tracker, null);
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new SlotSet("some_slot", "validated_value"));

        // check that validation succeed
        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));
    }

    /**
     * Test form validation with custom validator
     */
    @Test
    void validatePrefilledSlots() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {
                slotValidatorMap.put("some_slot", (value, dispatcher, tracker, domain) -> {
                    Map<String, Object> resultMap = new HashMap<>();
                    if("some_value".equals(value)) {
                        resultMap.put("some_slot", "validated_value");
                    } else {
                        resultMap.put("some_slot", null);
                    }
                    return resultMap;
                });
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("some_slot", "some_value")
                .addSlot("some_other_slot", "some_other_value")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_bad_value").build())
                        .text("some text")
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> events = customFormAction.activateFormIfRequired(null, tracker, null);

        // check that the form was activated and prefilled slots were validated
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new Form("some_form"));
        expectedEvents.add(new SlotSet("some_slot", "validated_value"));
        expectedEvents.add(new SlotSet("some_other_slot", "some_other_value"));

        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));

        //================
        events.addAll(customFormAction.validateIfRequired(null, tracker, null));

        // check that entities picked up in input overwrite prefilled slots
        expectedEvents = new ArrayList<>();
        expectedEvents.add(new Form("some_form"));
        expectedEvents.add(new SlotSet("some_slot", "validated_value"));
        expectedEvents.add(new SlotSet("some_other_slot", "some_other_value"));
        expectedEvents.add(new SlotSet("some_slot", null));

        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));
    }

    /**
     * Test validation results of from_trigger_intent slot mappings
     */
    @Test
    void validateTriggerSlots() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }

            @Override
            protected Map<String, List<AbstractSlotMapping>> slotMappings() {
                Map<String, List<AbstractSlotMapping>> slotMappingMap = new HashMap<>();

                slotMappingMap.put("some_slot", Arrays.asList(TriggerIntentSlotMapping.builder().intent("trigger_intent").value("some_value").build()));

                return slotMappingMap;
            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .latestMessage(MessageBuilder.builder()
                        .intent("trigger_intent", 1.0)
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> slotValues = customFormAction.validate(new CollectingDispatcher(), tracker, null);

        //check that the value was extracted on form activation
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new SlotSet("some_slot", "some_value"));

        assertTrue(slotValues.size() == expectedEvents.size() && slotValues.containsAll(expectedEvents));

        //=======================================
        tracker = TrackerBuilder.builder()
                .senderId("default")
                .latestMessage(MessageBuilder.builder()
                        .intent("trigger_intent", 1.0)
                        .build())
                .paused(false)
                .activeForm(FormBuilder.builder()
                        .name("some_form")
                        .validate(true)
                        .rejected(false)
                        .triggerMessage(MessageBuilder.builder()
                                .intent("trigger_intent", 1.0)
                                .build())
                        .build())
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.validate(new CollectingDispatcher(), tracker, null);
        //check that the value was not extracted after form activation
        assertTrue(slotValues.isEmpty());

        //=======================================
        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_other_slot")
                .latestMessage(MessageBuilder.builder()
                        .intent("some_other_intent", 1.0)
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .activeForm(FormBuilder.builder()
                        .name("some_form")
                        .validate(true)
                        .rejected(false)
                        .triggerMessage(MessageBuilder.builder()
                                .intent("trigger_intent", 1.0)
                                .build())
                        .build())
                .latestActionName("action_listen")
                .build();

        slotValues = customFormAction.validate(new CollectingDispatcher(), tracker, null);
        //check that validation failed gracefully
        expectedEvents = new ArrayList<>();
        expectedEvents.add(new SlotSet("some_other_slot", "some_other_value"));

        assertTrue(slotValues.size() == expectedEvents.size() && slotValues.containsAll(expectedEvents));
    }

    /**
     * Test activation form (if required)
     */
    @Test
    void activateIfRequired() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }
        };

        //================================================================================
        //   Form should be activated
        //================================================================================
        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .latestMessage(MessageBuilder.builder()
                        .intent("some_intent", 1.0)
                        .text("some text")
                        .build())
                .paused(false)
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> events = customFormAction.activateFormIfRequired(null, tracker, null);
        // check that the form was activated
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new Form("some_form"));

        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));

        //================================================================================
        //   When a form is already active, it shouldn't be activated again
        //================================================================================

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .paused(false)
                .activeForm(FormBuilder.builder()
                        .name("some_form")
                        .validate(true)
                        .rejected(false)
                        .build())
                .latestActionName("action_listen")
                .build();

        events = customFormAction.activateFormIfRequired(null, tracker, null);
        // check that the form was not activated again
        assertTrue(events.isEmpty());
    }

    /**
     * Test validate form (if required)
     */
    @Test
    void validateIfRequired() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }
        };

        //================================================================================
        //   A form validation should be performed
        //================================================================================
        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .activeForm(FormBuilder.builder()
                        .name("some_form")
                        .validate(true)
                        .rejected(false)
                        .build())
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> events = customFormAction.validateIfRequired(new CollectingDispatcher(), tracker, null);
        // check that validation was performed
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new SlotSet("some_other_slot", "some_other_value"));
        expectedEvents.add(new SlotSet("some_slot", "some_value"));

        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));

        //================================================================================
        //   A form validation should be skipped, because "validate=false"
        //================================================================================

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .activeForm(FormBuilder.builder()
                        .name("some_form")
                        .validate(false)
                        .rejected(false)
                        .build())
                .latestActionName("action_listen")
                .build();

        events = customFormAction.validateIfRequired(new CollectingDispatcher(), tracker, null);
        // check that validation was skipped because "validate=false"
        assertTrue(events.isEmpty());

        //================================================================================
        //   A form validation should be skipped, because previous action is not action_listen
        //================================================================================

        tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("requested_slot", "some_slot")
                .latestMessage(MessageBuilder.builder()
                        .addEntity(EntityBuilder.builder().entity("some_slot").value("some_value").build())
                        .addEntity(EntityBuilder.builder().entity("some_other_slot").value("some_other_value").build())
                        .build())
                .paused(false)
                .activeForm(FormBuilder.builder()
                        .name("some_form")
                        .validate(false)
                        .rejected(false)
                        .build())
                .latestActionName("some_form")
                .build();

        events = customFormAction.validateIfRequired(new CollectingDispatcher(), tracker, null);
        // check that validation was skipped because previous action is not action_listen
        assertTrue(events.isEmpty());
    }

    /**
     * Test early deactivation
     */
    @Test
    void earlyDeactivation() {
        AbstractFormAction customFormAction = new AbstractFormAction("some_form") {
            @Override
            protected List<String> requiredSlots(Tracker tracker) {
                return Arrays.asList("some_slot", "some_other_slot");
            }

            @Override
            List<AbstractEvent> validate(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
                return super.deactivate();
            }

            @Override
            protected List<AbstractEvent> submit(CollectingDispatcher dispatcher) {
                return null;
            }

            @Override
            protected void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap) {

            }
        };

        Tracker tracker = TrackerBuilder.builder()
                .senderId("default")
                .addSlot("some_slot", "some_value")
                .latestMessage(MessageBuilder.builder()
                        .intent("greet")
                        .build())
                .paused(false)
                .activeForm(FormBuilder.builder()
                        .name("some_form")
                        .validate(true)
                        .rejected(false)
                        .build())
                .latestActionName("action_listen")
                .build();

        List<AbstractEvent> events = customFormAction.run(null, tracker, null);
        // check that form was deactivated before requesting next slot
        List<AbstractEvent> expectedEvents = new ArrayList<>();
        expectedEvents.add(new Form(null));
        expectedEvents.add(new SlotSet("requested_slot", null));

        assertTrue(events.size() == expectedEvents.size() && events.containsAll(expectedEvents));
    }
}