package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {
    private static final Logger log = LoggerFactory.getLogger(JsonView.class);
    public static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        try {
            PrintWriter writer = response.getWriter();
            if (model.size() == 1) {
                model.values().forEach(value -> writer.write(asJson(value)));
                return;
            }

            writer.write(objectMapper.writeValueAsString(model));
        } catch (IOException e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }

    private String asJson(Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new IllegalArgumentException();
        }
    }
}
