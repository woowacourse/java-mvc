package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setHeader(HEADER_CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        if (model.size() > 1) {
            objectMapper.writeValue(response.getWriter(), model);
            return;
        }
        for (String key : model.keySet()) {
            objectMapper.writeValue(response.getWriter(), model.get(key));
        }
    }
}
