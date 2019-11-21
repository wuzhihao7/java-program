package com.technologysia;

import org.nustaq.serialization.FSTConfiguration;

/**
 * @author house
 */
public class FstSerializerUtil {
    static FSTConfiguration configuration = FSTConfiguration.createDefaultConfiguration();

    private FstSerializerUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data) {
        return (T) configuration.asObject(data);
    }

    public static <T> byte[] serialize(T obj) {
        return configuration.asByteArray(obj);
    }
}
