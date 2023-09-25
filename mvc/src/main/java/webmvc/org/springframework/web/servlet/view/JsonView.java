package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public void render(Map<String, ?> model,HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter printWriter = response.getWriter();
        String viewValue = objectMapper.writeValueAsString(createResult(model));
        printWriter.write(viewValue);
    }

    private Object createResult(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values();
        }
        return model;
    }
}
