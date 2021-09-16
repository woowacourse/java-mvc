package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        PrintWriter out = response.getWriter();

        out.print(makeJson(model));
        out.flush();
    }

    private String makeJson(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            Object key = model.keySet().toArray()[0];
            return objectMapper.writeValueAsString(model.get(key));
        }
        return objectMapper.writeValueAsString(model);
    }
}
