# Rasa Java-SDK

Java SDK for the development of custom actions for [Rasa](https://rasa.com/).

When you want to do something advanced (e.g. execute external API, execute some business logic, etc.) by your Rasa's bot, you should use a [custom actions](https://rasa.com/docs/rasa/core/actions/#custom-actions).

With this SDK you can easily create the custom actions, written in Java. You can focus only on bussiness logic, everythig else will be done by the SDK.

In order to run Rasa's custom action, you need to have an action server. Make sure your action server is running and the URL for the action server is correct in the `endpoints.yml` file (see [custom action](https://rasa.com/docs/rasa/core/actions/#custom-actions)):

For example:

```yml
action_endpoint:
  url: "http://localhost:5055/webhook"
```

### Java version

SDK is compatibile with Java 1.8+

### Maven Repository

SDK is available in the Central Maven Reposity:

```xml
<dependency>
  <groupId>io.github.rbajek</groupId>
  <artifactId>rasa-java-sdk</artifactId>
  <version>1.0.1</version>
</dependency>
```

## Compatibility with Rasa

| SDK version    | compatible Rasa version           |
|----------------|-----------------------------------|
| `1.0.x`        | `>=1.4.x`              |

## Usage

Let's assume that we have a restaurant bot and when the user says "show me a Mexican restaurant", our bot should return the restaurant from the database.

So, we can create a custom action (let's called "action_check_restaurants") which might look like this:

```java
import io.github.rbajek.rasa.sdk.CollectingDispatcher;
import io.github.rbajek.rasa.sdk.action.Action;
import io.github.rbajek.rasa.sdk.dto.Domain;
import io.github.rbajek.rasa.sdk.dto.Tracker;
import io.github.rbajek.rasa.sdk.dto.event.AbstractEvent;
import io.github.rbajek.rasa.sdk.dto.event.SlotSet;

import java.util.Arrays;
import java.util.List;

public class ActionCheckRestaurants implements Action {

    @Override
    public String name() {
        return "action_check_restaurants";
    }
	
    private String readFromRestaurantDatabase(String cuisine) {
        //TODO should be implemented
	return null;
    }

    @Override
    public List<AbstractEvent> run(CollectingDispatcher collectingDispatcher, Tracker tracker, Domain domain) {
        String cuisine = tracker.getSlotValue("cuisine", String.class);

        // read from database
        String restaurant = readFromRestaurantDatabase(cuisine);
		
	// return result of the action
        return Arrays.asList(new SlotSet("matches", restaurant));
    }
}
```
Currently, SDK supports two types of custom actions:
- **general** - which corresponds to [Rasa Custom Action](https://rasa.com/docs/rasa/core/actions/#custom-actions). To use this kind of actions, you can create a Java class which implement the ``io.github.rbajek.rasa.sdk.action.Action`` interface. [Here](https://github.com/rbajek/rasa-java-action-server/blob/master/src/main/java/io/github/rbajek/rasa/action/server/action/custom/joke/ActionJoke.java) you can find an example (based on [original example](https://rasa.com/docs/rasa/user-guide/running-rasa-with-docker/#creating-a-custom-action))
- **forms** - which corresponds to [Rasa Forms](https://rasa.com/docs/rasa/core/forms/). Ths kind of actions should extends the ``io.github.rbajek.rasa.sdk.action.form.AbstractFormAction``. [Here](https://github.com/rbajek/rasa-java-action-server/blob/master/src/main/java/io/github/rbajek/rasa/action/server/action/custom/form/restaurant/RestaurantFormAction.java) you can find an example (which implement functionality of [Restaurant Form](https://blog.rasa.com/building-contextual-assistants-with-rasa-formaction/)).

Afterwards, we have to register our action within the `ActionExecutor` (which is part of the SDK) and run it. The response should be return back to Rasa as a JSON format.

To run the custom action, Rasa needs the action server, which exposes a [REST API](https://rasa.com/docs/rasa/api/action-server/),which can be executed to run custom action. So, we need to have the REST endpoint in our system, which can consume the Rasa's JSON request, run the custom action, and return response in JSON format.

The simple REST endpoint which can handle requests from Rasa is below:.

```java
package io.example;

import io.github.rbajek.rasa.sdk.ActionExecutor;
import io.github.rbajek.rasa.sdk.dto.ActionRequest;
import io.github.rbajek.rasa.sdk.dto.ActionResponse;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/webhook")
public class RasaWebhook {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public ActionResponse handleAction(ActionRequest request) {
	// create instance of the action executor
       ActionExecutor actionExecutor = new ActionExecutor();
	   
	// register custom action
       actionExecutor.registerAction(new ActionCheckRestaurants());
       
	// run custom action and return result
	return actionExecutor.run(actionRequest);
    }
}
```

and the last but not least - we need to set the URL of our endpoint in the "endpoints.yml":

```yml
action_endpoint:
  url: "http://<IP>:<PORT>/webhook"
```

### Action Server

To run the custom actions, it's required to have REST endpoint which can handle requests from Rasa. A system which does this is called `Action Server`. You can use your existing system as action server or you can use the [Rasa Java Action Server](https://github.com/rbajek/rasa-java-action-server) as a starting point for your server.

#### 1. Using your existing system

If you have already your own system, which can expouse the REST API, you can simple use it.

1. Add the dependency to the Java SDK:

    SDK is available in the Central Maven Reposity:

    ```xml
    <dependency>
      <groupId>io.github.rbajek</groupId>
      <artifactId>rasa-java-sdk</artifactId>
      <version>1.0.1</version>
    </dependency>
    ```
2. Expouse the REST endpoint, which can received calls from Rasa. This enpoint should handle POST requests and map the input JSON request to

   ```
   io.github.rbajek.rasa.sdk.dto.ActionRequest
   ```
   
   register your custom action within the 
   
   ```
   io.github.rbajek.rasa.sdk.ActionExecutor
   ```
   
   run it and return response as the JSON format
   
#### 2. Rasa Java Action Server

If you don't have your own system or if you would like to start from strach, then you can use the already created [Rasa Java Action Server](https://github.com/rbajek/rasa-java-action-server) as a starting point.
