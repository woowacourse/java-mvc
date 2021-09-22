package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();

        writer.println(toJsonString(model));
        writer.close();
    }

    private String toJsonString(Map<String, ?> model) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(toShowingObject(model));
    }

    private Object toShowingObject(Map<String, ?> model) {
        if (model.size() == 1) {
            return model.values().toArray()[0];
        }
        return model;
    }
}
