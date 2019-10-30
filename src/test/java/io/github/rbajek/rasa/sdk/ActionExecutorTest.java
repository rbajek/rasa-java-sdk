package io.github.rbajek.rasa.sdk;

import io.github.rbajek.rasa.sdk.action.Action;
import io.github.rbajek.rasa.sdk.dto.ActionRequest;
import io.github.rbajek.rasa.sdk.dto.ActionResponse;
import io.github.rbajek.rasa.sdk.dto.Domain;
import io.github.rbajek.rasa.sdk.dto.Tracker;
import io.github.rbajek.rasa.sdk.dto.event.AbstractEvent;
import io.github.rbajek.rasa.sdk.dto.event.SlotSet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ActionExecutorTest {

    private static final String ACTION_NAME = "custom_action";

    @Test
    void run() {
        ActionExecutor executor = new ActionExecutor();
        executor.registerAction(new CustomAction());

        ActionRequest actionRequest = new ActionRequest();
        actionRequest.setNextAction(ACTION_NAME);
        actionRequest.setVersion(VersionChecker.SUPPORTED_VERSION);
        ActionResponse events = executor.run(actionRequest);
        assertTrue(events.getEvents().size() == 1);
        Assertions.assertEquals(new SlotSet("test", "test"), events.getEvents().get(0));
    }

    private static class CustomAction implements Action {

        @Override
        public String name() {
            return ACTION_NAME;
        }

        public String someCommonFeature() {
            return "test";
        }

        @Override
        public List<AbstractEvent> run(CollectingDispatcher dispatcher, Tracker tracker, Domain domain) {
            return Arrays.asList(new SlotSet("test", someCommonFeature()));
        }
    }
}