package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (model.size() == 1) {
            Object key = model.keySet().toArray()[0];
            makeResponse(response, model.get(key));
            return;
        }
        makeResponse(response, model);
    }

    private void makeResponse(HttpServletResponse response, Object value) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(value);
        out.print(json);
        out.flush();
    }
}
