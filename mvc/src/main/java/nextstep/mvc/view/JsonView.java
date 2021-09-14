package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.IOException;
import java.util.Map;

public class JsonView implements View {

    private static final String CONTENT_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;
    private static final JsonEncoding encoding = JsonEncoding.UTF8;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        try (JsonGenerator generator = getJsonGenerator(response)) {
            response.setContentType(CONTENT_TYPE);
            String jsonResult = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(model);
            generator.writeRaw(jsonResult);
            generator.flush();
        }
    }

    private JsonGenerator getJsonGenerator(HttpServletResponse response) throws IOException {
        return objectMapper.getFactory().createGenerator(response.getOutputStream(), encoding);
    }
}
