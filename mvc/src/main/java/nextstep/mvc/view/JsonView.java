package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonView implements View {

    private static final Logger log = LoggerFactory.getLogger(JsonView.class);

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
        });
        String json = objectMapper.writeValueAsString(model);

        response.setHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(json.getBytes());
        outputStream.flush();
    }
}
