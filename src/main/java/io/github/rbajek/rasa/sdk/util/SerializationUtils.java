package io.github.rbajek.rasa.sdk.util;

import io.github.rbajek.rasa.sdk.exception.RasaException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;

public class SerializationUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Clone deep using JSON serialization
     *
     * @param object an object which should be cloned
     * @param <T> type of the object which should be cloned
     * @return the cloned object
     */
    public static <T> T deepClone(final T object) {
        try {
            return (T) OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(object), object.getClass());
        } catch (IOException e) {
            throw new RasaException(e);
        }
    }

    /**
     * Clone deep using Java serialization
     *
     * @param object an object which should be cloned
     * @param <T> type of the object which should be cloned
     * @return the cloned object
     */
    public static <T extends Serializable> T deepClone(final T object)  {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serialize(object, baos);
        return deserialize(new ByteArrayInputStream(baos.toByteArray()));
    }

    private static void serialize(final Serializable obj, final OutputStream outputStream) {
        try (ObjectOutputStream out = new ObjectOutputStream(outputStream)) {
            out.writeObject(obj);
        } catch (final IOException ex) {
            throw new RasaException(ex);
        }
    }

    private static <T> T deserialize(final InputStream inputStream) {
        try (ObjectInputStream in = new ObjectInputStream(inputStream)) {
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RasaException(e);
        }
    }
}
