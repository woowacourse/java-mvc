package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        Set<String> keySet = model.keySet();
        for (String key : keySet) {
            ObjectMapper objectMapper = new ObjectMapper();
            Object value = model.get(key);
            String result = objectMapper.writeValueAsString(value);
            PrintWriter writer = response.getWriter();
            writer.write(result);
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
