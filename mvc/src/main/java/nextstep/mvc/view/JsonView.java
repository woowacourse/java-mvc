package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonEncoding encoding = JsonEncoding.UTF8;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Object modelData = getModelData(model);
        final OutputStream stream = response.getOutputStream();

        Class<?> serializationView = (Class<?>) model.get(com.fasterxml.jackson.annotation.JsonView.class.getName());
        writeContent(serializationView, stream, modelData);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    private Object getModelData(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values()
                    .iterator()
                    .next();
        }
        return model;
    }

    private void writeContent(final Class<?> serializationView, final OutputStream stream, final Object value) throws IOException {
        try (final JsonGenerator generator = this.objectMapper.getFactory().createGenerator(stream, this.encoding)) {
            final ObjectWriter objectWriter = this.objectMapper.writerWithView(serializationView);
            objectWriter.writeValue(generator, value);
        }
    }
}
