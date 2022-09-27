package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String json = getJson(model);
        writeJson(response, json);
    }

    private String getJson(final Map<String, ?> model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(model);
    }

    private void writeJson(final HttpServletResponse response, final String json) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(json);
    }
}
