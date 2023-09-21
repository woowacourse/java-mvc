package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();
        if (model.size() == 1) {
            renderSingleValue(model, writer);
            return;
        }
        renderEntries(model, writer);
    }

    private void renderSingleValue(Map<String, ?> model, PrintWriter writer) {
        model.forEach((key, value) -> {
            log.debug("attribute name : {}, value : {}", key, value);
            writer.write(value.toString());
        });
    }

    private void renderEntries(Map<String, ?> model, PrintWriter writer)
            throws JsonProcessingException {
        String jsonModel = OBJECT_MAPPER.writeValueAsString(model);
        writer.write(jsonModel);
    }
}
