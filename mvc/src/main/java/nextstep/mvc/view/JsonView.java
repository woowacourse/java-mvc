package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger LOG = LoggerFactory.getLogger(JsonView.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        if (model.size() <= 1) {
            model.keySet().forEach(key -> {
                LOG.debug("attribute name : {}, value : {}", key, model.get(key));
                write(model.get(key), response);
            });
            return;
        }

        write(model, response);
    }

    private void write(Object object, HttpServletResponse response) {
        try {
            PrintWriter writer = response.getWriter();
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            response.setCharacterEncoding("UTF-8");
            String jsonString = OBJECT_MAPPER.writeValueAsString(object);
            writer.print(jsonString);
            writer.flush();
        } catch (Exception e) {
            throw new IllegalStateException();
        }
    }
}
