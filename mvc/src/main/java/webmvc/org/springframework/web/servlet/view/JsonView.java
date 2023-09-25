package webmvc.org.springframework.web.servlet.view;

import java.io.PrintWriter;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final int SINGLE_JSON_SIZE = 1;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter = response.getWriter();
        String json = objectMapper.writeValueAsString(createResult(model));
        printWriter.write(json);
    }

    private Object createResult(Map<String, ?> model) {
        if (model.size() == SINGLE_JSON_SIZE) {
            return model.values();
        }
        return model;
    }

}
