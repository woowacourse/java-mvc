package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectWriter objectWriter = new ObjectMapper().writer();

    public JsonView() {
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        final PrintWriter out = response.getWriter();
        String json;
        if (model.size() == 1) {
            json = objectWriter.writeValueAsString(model.values().toArray()[0]);
            out.print(json);
            return;
        }

        json = objectWriter.writeValueAsString(model);
        out.print(json);
    }

    @Override
    public String viewName() {
        return null;
    }
}
