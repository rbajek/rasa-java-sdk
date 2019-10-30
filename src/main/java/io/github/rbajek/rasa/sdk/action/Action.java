package io.github.rbajek.rasa.sdk.action;

import io.github.rbajek.rasa.sdk.CollectingDispatcher;
import io.github.rbajek.rasa.sdk.dto.Domain;
import io.github.rbajek.rasa.sdk.dto.Tracker;
import io.github.rbajek.rasa.sdk.dto.event.AbstractEvent;

import java.util.List;
import java.util.Map;

/**
 * Next action to be taken in response to a dialogue state.
 *
 * @author Rafał Bajek
 */
public interface Action {

    /**
     * Unique identifier of this action.
     *
     * @return a name of this action
     */
    String name();

    /**
     * Execute the side effects of this action
     *
     * @param dispatcher the dispatcher which is used to send messages back to the user.
     *                   Use {@link CollectingDispatcher#utterMessage(String, Map)} or any other method.
     * @param tracker the state tracker for the current user. You can access slot values using <code>tracker.getSlots().get(slotName)</code> (see: {@link Tracker}),
     *                the most recent user message is <code>tracker.getLatestMessage().getText()</code> (see {@link Tracker}) and any other property.
     * @param domain the bot's domain
     * @return A list of {@link AbstractEvent} instances that is returned through the endpoint
     */
    List<AbstractEvent> run(CollectingDispatcher dispatcher, Tracker tracker, Domain domain);
}
