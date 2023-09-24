package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        if (model.size() > 0) {
            final ServletOutputStream outputStream = response.getOutputStream();

            final String responseBody = getResponseBody(model);
            final byte[] bytes = responseBody.getBytes();
            outputStream.write(bytes);
            response.setContentLength(bytes.length);
        }
    }

    private String getResponseBody(final Map<String, ?> model) throws Exception {
        if (model.size() == 1) {
            return model.values().toArray()[0].toString();
        }
        final ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(model);
    }

    @Override
    public String getName() {
        return "";
    }
}
