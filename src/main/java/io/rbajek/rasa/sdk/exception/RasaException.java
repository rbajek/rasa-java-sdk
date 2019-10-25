package io.rbajek.rasa.sdk.exception;

public class RasaException extends RuntimeException {

    public RasaException(String message) {
        super(message);
    }

    public RasaException(Throwable cause) {
        super(cause);
    }
}
