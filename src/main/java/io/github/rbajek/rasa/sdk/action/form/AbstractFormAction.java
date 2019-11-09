package io.github.rbajek.rasa.sdk.action.form;

import io.github.rbajek.rasa.sdk.CollectingDispatcher;
import io.github.rbajek.rasa.sdk.action.Action;
import io.github.rbajek.rasa.sdk.action.form.slot.mapper.AbstractSlotMapping;
import io.github.rbajek.rasa.sdk.action.form.slot.mapper.EntitySlotMapping;
import io.github.rbajek.rasa.sdk.action.form.slot.mapper.IntentSlotMapping;
import io.github.rbajek.rasa.sdk.action.form.slot.mapper.TriggerIntentSlotMapping;
import io.github.rbajek.rasa.sdk.dto.Domain;
import io.github.rbajek.rasa.sdk.dto.Tracker;
import io.github.rbajek.rasa.sdk.dto.event.AbstractEvent;
import io.github.rbajek.rasa.sdk.dto.event.Form;
import io.github.rbajek.rasa.sdk.dto.event.SlotSet;
import io.github.rbajek.rasa.sdk.exception.ActionExecutionRejectionException;
import io.github.rbajek.rasa.sdk.exception.RasaException;
import io.github.rbajek.rasa.sdk.util.CollectionsUtils;
import io.github.rbajek.rasa.sdk.util.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * An abstract form action class
 *
 * @author Rafał Bajek
 */
public abstract class AbstractFormAction implements Action {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFormAction.class);

    /**
     * This slot is used to store information needed to do the form handling
     */
    protected static final String REQUESTED_SLOT = "requested_slot";

    /**
     * Unique identifier of the form
     */
    private final String formName;

    /**
     * Map of slot validators
     */
    private final Map<String, ValidateSlot> slotValidatorMap = new HashMap<>();

    public AbstractFormAction(String formName) {
        this.formName = formName;
        registerSlotsValidators(slotValidatorMap);
    }

    /**
     * A list of required slots that the form has to fill.
     *
     * Use "tracker" to request different list of slots
     * depending on the state of the dialogue
     *
     * @param tracker a {@link Tracker} object
     * @return list of required slots
     */
    protected abstract List<String> requiredSlots(Tracker tracker);

    /**
     * Define what the form has to do
     * after all required slots are filled
     *
     * @param dispatcher a {@link CollectingDispatcher} object
     * @return list of events
     */
    protected abstract List<AbstractEvent> submit(CollectingDispatcher dispatcher);

    /**
     * Register a slot validator
     *
     * @param slotValidatorMap map of slot validators which should be filled out in a particular class
     */
    protected abstract void registerSlotsValidators(Map<String, ValidateSlot> slotValidatorMap);

    /**
     * <p>A Map to mapping required slots.</p>
     *
     * <p>Options:</p>
     * <ul>
     * <li>an extracted entity</li>
     * <li>intent: value pairs</li>
     * <li>trigger_intent: value pairs</li>
     * <li>a whole message</li>
     * </ul>
     *
     * <p>or a list of them, where the first match will be picked</p>
     *
     * <p>Empty map is converted to a mapping of
     * the slot to the extracted entity with the same name</p>
     *
     * @return Map of slots with mappings
     */
    protected Map<String, List<AbstractSlotMapping>> slotMappings() {
        return Collections.emptyMap();
    }

    /**
     * Check whether user intent matches intent conditions
     *
     * @param requestedSlotMapping requested slot mapping
     * @param tracker a tracker object
     * @return <code>true</code> - if the user intent matches intent conditions. Otherwise - <code>false</code>
     */
    private boolean intentIsDesired(AbstractSlotMapping requestedSlotMapping, Tracker tracker) {
        List<String> mappingIntents = requestedSlotMapping.getIntent();
        List<String> mappingNotIntents = requestedSlotMapping.getNotIntent();
        String intent = tracker.getLatestMessage().getIntent() != null ? tracker.getLatestMessage().getIntent().getName() : null;

        boolean intentNotBlacklisted = CollectionsUtils.isEmpty(mappingIntents) && mappingNotIntents.contains(intent) == false;

        return intentNotBlacklisted || mappingIntents.contains(intent);
    }

    /**
     * Logs the values of all required slots before submitting the form.
     * 
     * @param tracker a {@link Tracker} object
     */
    private void logFormSlots(Tracker tracker) {
        List<String> requiredSlots = requiredSlots(tracker);
        if(CollectionsUtils.isNotEmpty(requiredSlots) && tracker.hasSlots()) {
            StringBuilder slotValues = new StringBuilder();
            requiredSlots.forEach(slotName -> {
                slotValues.append("\n").append("\t").append(slotName).append(": ").append(tracker.getSlotValue(slotName));
            });
            LOGGER.debug("No slots left to request, all required slots are filled:{}", slotValues);
        }
        LOGGER.debug("There are no any slots to requests");
    }

    /**
     * Activate form if the form is called for the first time
     *
     * If activating, validate any required slots that were filled before
     * form activation and return "Form" event with the name of the form, as well
     *
     * @param dispatcher a {@link CollectingDispatcher} object
     * @param tracker a {@link Tracker} object
     * @param domain a {@link Domain} object
     * @return list of events
     */
    List<AbstractEvent> activateFormIfRequired(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        List<AbstractEvent> events = new ArrayList<>();

        if(tracker.hasActiveForm()){
            LOGGER.debug("The form '{}' is active", tracker.getActiveForm().getName());
        } else {
            LOGGER.debug("There is no active form");
        }

        if(tracker.hasActiveForm() && this.name().equals(tracker.getActiveForm().getName())) {
            return new ArrayList<>();
        } else {
            LOGGER.debug("Activated the form '{}'", this.name());
            events.add(new Form(this.name()));

            //collect values of required slots filled before activation
            Map<String, Object> preFilledSlots = new HashMap<>();
            List<String> requiredSlots = requiredSlots(tracker);
            if(CollectionsUtils.isNotEmpty(requiredSlots)) {
                requiredSlots.forEach(slotName -> {
                    if (tracker.hasSlots() && !shouldRequestSlot(tracker, slotName)) {
                        preFilledSlots.put(slotName, tracker.getSlotValue(slotName));
                    }
                });
            }

            if(!preFilledSlots.isEmpty()) {
                LOGGER.debug("Validating pre-filled required slots: {}", preFilledSlots);
                events.addAll(validateSlots(preFilledSlots, dispatcher, tracker, domain));
            } else {
                LOGGER.debug("No pre-filled required slots to validate.");
            }
        }
        return events;
    }

    /**
     * Return a list of events from "validate(...)" method if validation is required:
     * - the form is active
     * - the form is called after "action_listen"
     * - form validation was not cancelled
     *
     * @param dispatcher a {@link CollectingDispatcher} object
     * @param tracker a {@link Tracker} object
     * @param domain a {@link Domain} object
     * @return list of events
     */
    List<AbstractEvent> validateIfRequired(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        if("action_listen".equals(tracker.getLatestActionName()) && (tracker.getActiveForm() == null || tracker.getActiveForm().shouldValidate(true))) {
            LOGGER.debug("Validating user input '{}'", tracker.getLatestMessage());
            return validate(dispatcher, tracker, domain);
        }
        LOGGER.debug("Skipping validation");
        return Collections.emptyList();
    }

    /**
     * Extract and validate value of requested slot.
     *
     * If nothing was extracted reject execution of the form action.
     * Subclass this method to add custom validation and rejection logic
     *
     * @param dispatcher a {@link CollectingDispatcher} object
     * @param tracker a {@link Tracker} object
     * @param domain a {@link Domain} object
     * @return list of events
     */
    List<AbstractEvent> validate(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        // extract other slots that were not requested
        // but set by corresponding entity or trigger intent mapping

        Map<String, Object> slotValues = extractOtherSlots(dispatcher, tracker, domain);

        // extract requested slot
        if(tracker.hasSlotValue(REQUESTED_SLOT)) {
            Object slotToFill = tracker.getSlotValue(REQUESTED_SLOT);
            slotValues.putAll(extractRequestedSlot(dispatcher, tracker, domain));

            if(CollectionsUtils.isEmpty(slotValues)) {
                // reject to execute the form action
                // if some slot was requested but nothing was extracted
                // it will allow other policies to predict another action
                throw new ActionExecutionRejectionException("Failed to extract slot '" + slotToFill + "' with action '" + name() + "'");
            }
        }

        LOGGER.debug("Validating extracted slots: {}", slotValues);
        return validateSlots(slotValues, dispatcher, tracker, domain);
    }

    /**
     * Request the next slot and utter template if needed
     *
     * @param dispatcher a {@link CollectingDispatcher} object
     * @param tracker a {@link Tracker} object
     * @param domain a {@link Domain} object
     * @return an event
     */
    private AbstractEvent requestNextSlot(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        List<String> requiredSlots = requiredSlots(tracker);
        if(requiredSlots != null) {
            for (String slotName : requiredSlots) {
                if(shouldRequestSlot(tracker, slotName)) {
                    LOGGER.debug("Request next slot '{}'", slotName);
                    dispatcher.utterTemplate("utter_ask_" + slotName, tracker.hasSlots() ? tracker.getSlots() : Collections.emptyMap());
                    return new SlotSet(REQUESTED_SLOT, slotName);
                }
            };
        }
        // no more required slots to fill
        return null;
    }

    /**
     * Return "Form" event with null as name to deactivate the form and reset the requested slot
     *
     * @return list of events
     */
    protected List<AbstractEvent> deactivate() {
        LOGGER.debug("Deactivating the form '{}'", name());
        return Arrays.asList(new Form(null), new SlotSet(REQUESTED_SLOT, null));
    }

    /**
     * Extract entities for given name
     *
     * @param entityName an entity name
     * @return an extracted value of the given entity name
     */
    private String getEntityValue(String entityName, Tracker tracker) {
        List<String> entityValues = tracker.getLatestEntityValues(entityName);
        return CollectionsUtils.isNotEmpty(entityValues) ? entityValues.get(0) : null;
    }

    /**
     * Extract the values of the other slots if they are set by corresponding entities from the user input
     * else return None
     *
     * @param dispatcher a {@link CollectingDispatcher} object
     * @param tracker a {@link Tracker} object
     * @param domain a {@link Domain} object
     * @return map of the other slots with values
     */
    Map<String, Object> extractOtherSlots(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        Map<String, Object> slotValues = new HashMap<>();

        List<String> requiredSlots = requiredSlots(tracker);
        if(requiredSlots != null && !requiredSlots.isEmpty()) {
            // look for other slots
            for (String slotName : requiredSlots) {
                if(!slotName.equals(tracker.getSlotValue(REQUESTED_SLOT))) {
                    List<AbstractSlotMapping> otherSlotMappings = getMappingsForSlot(slotName);
                    for (AbstractSlotMapping otherSlotMapping : otherSlotMappings) {
                        // check whether the slot should be filled by entity with the same name

                        boolean shouldFillEntitySlot = otherSlotMapping.isEntitySlotMappingType() &&
                                ((EntitySlotMapping) otherSlotMapping).getEntity().equals(slotName) &&
                                intentIsDesired(otherSlotMapping, tracker);

                        // check whether the slot should be filled from trigger intent mapping
                        boolean shouldFillTriggerSlot = (!tracker.hasActiveForm() || name().equals(tracker.getActiveForm().getName()) == false) &&
                                otherSlotMapping.isTriggerIntentSlotMappingType() &&
                                intentIsDesired(otherSlotMapping, tracker);

                        Object value = null;
                        if (shouldFillEntitySlot) {
                            value = getEntityValue(slotName, tracker);
                        } else if (shouldFillTriggerSlot) {
                            value = ((TriggerIntentSlotMapping) otherSlotMapping).getValue();
                        }

                        if(value != null) {
                            LOGGER.debug("Extracted '{}' for extra slot '{}'", value, slotName);
                            slotValues.put(slotName, value);
                            return slotValues;
                        }

                    }
                }
            }
        }
        return slotValues;
    }

    /**
     * Extract the value of requested slot from a user input
     *
     * @param dispatcher a {@link CollectingDispatcher} object
     * @param tracker a {@link Tracker} object
     * @param domain a {@link Domain} object
     * @return map of the requested slot with extracted value
     */
    protected Map<String, Object> extractRequestedSlot(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        if(tracker.hasSlotValue(REQUESTED_SLOT) == false) {
            return Collections.emptyMap();
        }

        String slotToFill = tracker.getSlotValue(REQUESTED_SLOT, String.class);
        LOGGER.debug("Trying to extract requested slot '{}' ...", slotToFill);

        //get mapping for requested slot
        List<AbstractSlotMapping> requestedSlotMappings = getMappingsForSlot(slotToFill);
        for (AbstractSlotMapping requestedSlotMapping : requestedSlotMappings) {
            LOGGER.debug("Got mapping '{}'", requestedSlotMapping);
            if(intentIsDesired(requestedSlotMapping, tracker)) {
                Object value = null;
                switch(requestedSlotMapping.getType()) {
                    case ENTITY:
                        value = getEntityValue(EntitySlotMapping.class.cast(requestedSlotMapping).getEntity(), tracker);
                        break;

                    case INTENT:
                        value = IntentSlotMapping.class.cast(requestedSlotMapping).getValue();
                        break;

                    case TEXT:
                        value = tracker.getLatestMessage().getText();
                        break;

                    default:
                        throw new RasaException("Provided slot mapping type ('" + requestedSlotMapping.getType() + "') is not supported");
                }

                if(value != null) {
                    LOGGER.debug("Successfully extracted '{}' for requested slot '{}'", value, slotToFill);
                    Map<String, Object> resultMap = new HashMap<>();
                    resultMap.put(slotToFill, value);
                    return resultMap;
                }
            }
        }
        LOGGER.debug("Failed to extract requested slot '{}'", slotToFill);
        return Collections.emptyMap();
    }

    /**
     * Get mappings for requested slot.
     *
     * If None, map requested slot to an entity with the same name
     *
     * @param slotToFill a slot which should be filled
     * @return list of slot mappings
     */
    private List<AbstractSlotMapping> getMappingsForSlot(String slotToFill) {
        List<AbstractSlotMapping> requestedSlotMappings = slotMappings().getOrDefault(slotToFill, Arrays.asList(EntitySlotMapping.builder(slotToFill).build()));

        // check provided slot mappings
        requestedSlotMappings.forEach(requestedSlotMapping -> {
            if(requestedSlotMapping.getType() == null) {
                throw new RasaException("Provided incompatible slot mapping");
            }
        });

        return requestedSlotMappings;
    }

    /**
     * Validate slots using helper validation functions.
     *
     * Call particular validator for each slot, value pair to be validated.
     * If the particular validator is not implemented, set the slot to the value.
     *
     * @param slotsMap map of slots with values
     * @param dispatcher a {@link CollectingDispatcher} object
     * @param tracker a {@link Tracker} object
     * @param domain a {@link Domain} object
     * @return list of event
     */
    private List<AbstractEvent> validateSlots(Map<String, Object> slotsMap, CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        Map<String, Object> validationOutput = new HashMap<>();
        slotsMap.forEach((slotName, slotValue) -> {
            if(this.slotValidatorMap.containsKey(slotName)) {
                Map<String, Object> result = this.slotValidatorMap.get(slotName).validateAndConvert(slotValue, dispatcher, tracker, domain);
                validationOutput.putAll(result);
            }
        });

        slotsMap.putAll(validationOutput);

        // validation succeed, set slots to extracted values
        List<AbstractEvent> events = new ArrayList<>();
        slotsMap.forEach((slotName, slotValue) -> {
            events.add(new SlotSet(slotName, slotValue));
        });
        return events;
    }

    /**
     * Check whether form action should request given slot
     *
     * @param tracker a {@link Tracker} object
     * @param slotName a slot name
     * @return <code>true</code> - if the given slot should be requested. Otherwise - <code>false</code>
     */
    private boolean shouldRequestSlot(Tracker tracker, String slotName) {
        return tracker.hasSlotValue(slotName) == false;
    }

    @Override
    public String name() {
        return this.formName;
    }

    /**
     * Execute the side effects of this form.
     *
     * Steps:
     *  - activate if needed
     *  - validate user input if needed
     *  - set validated slots
     *  - utter_ask_{slot} template with the next required slot
     *  - submit the form if all required slots are set
     *  - deactivate the form
     * @return list of events
     */
    @Override
    public List<AbstractEvent> run(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
        // activate the form
        List<AbstractEvent> events = activateFormIfRequired(dispatcher, tracker, domain);

        // validate user input
        events.addAll(validateIfRequired(dispatcher, tracker, domain));

        // check that the form wasn't deactivated in validation
        if(events.stream().filter(event -> event instanceof Form).noneMatch(event -> Form.class.cast(event).isNotActive())) {
            // create temp tracker with populated slots from `validate` method

            // deep clone using JSON serialization
            Tracker tempTracker = SerializationUtils.deepClone(tracker); //JsonParser.parse(JsonParser.jsonAsString(tracker), Tracker.class);
            events.forEach(event -> {
                if(SlotSet.class.isInstance(event)) {
                    SlotSet slotSetEvent =  SlotSet.class.cast(event);
                    tempTracker.addSlot(slotSetEvent.getName(), slotSetEvent.getValue());
                }
            });

            AbstractEvent nextSlotEvent = requestNextSlot(dispatcher, tempTracker, domain);
            if(nextSlotEvent != null) {
                // request next slot
                events.add(nextSlotEvent);
            } else {
                // there is nothing more to request, so we can submit
                logFormSlots(tempTracker);
                LOGGER.debug("Submitting the form '{}'", name());
                List<AbstractEvent> submitEvents = submit(dispatcher);
                if(CollectionsUtils.isNotEmpty(submitEvents)) {
                    events.addAll(submitEvents);
                }
                // deactivate the form after submission
                events.addAll(deactivate());
            }
        }
        return events;
    }

    /**
     * A validator slot interface
     *
     * @author Rafał Bajek
     */
    public interface ValidateSlot {

        /**
         * Validate slot value and set a new value if required
         *
         * @param value current slot value
         * @param dispatcher dispatcher object
         * @param tracker tracker object
         * @param domain domain object
         * @return Map of slots value, where: key=slotName, value=slotValue
         */
        Map<String, Object> validateAndConvert(Object value, CollectingDispatcher dispatcher, Tracker tracker, Domain domain);
    }
}
