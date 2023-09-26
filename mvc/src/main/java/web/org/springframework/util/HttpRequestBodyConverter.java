package web.org.springframework.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

public class HttpRequestBodyConverter {

    private static final ObjectReader reader;
    private static final ObjectWriter writer;

    static {
        final ObjectMapper mapper = new ObjectMapper();
        reader = mapper.reader();
        writer = mapper.writer();
    }

    public static Object serialize(final String json, final Class<?> target) throws Exception {
        return reader.readValue(json, target);
    }

    public static String deserialize(final Object object) throws Exception {
        return writer.writeValueAsString(object);
    }
}
