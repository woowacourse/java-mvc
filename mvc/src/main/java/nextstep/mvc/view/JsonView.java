package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();

        if (model.size() == 1) {
            Object obj = model.values().iterator().next();
            String jsonString = objectMapper.writeValueAsString(obj);
            writer.write(jsonString);
            return;
        }
        String jsonString = objectMapper.writeValueAsString(model);
        writer.write(jsonString);
    }
}
