package io.github.rbajek.rasa.sdk.exception;

/**
 * Base Exception
 * 
 * @author Rafał Bajek
 */
public class RasaException extends RuntimeException {

    public RasaException(String message) {
        super(message);
    }

    public RasaException(Throwable cause) {
        super(cause);
    }
}
