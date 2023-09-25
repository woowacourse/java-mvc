package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    public static final int SINGLE_DATA_RESPONSE = 1;
    public static final int NOT_RESPONSE_BODY = 0;
    private static final String JSON_CONTENT_TYPE = "Application/json";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        Set<String> responseData = model.keySet();
        if (responseData.size() == NOT_RESPONSE_BODY) {
            return;
        }
        if (responseData.size() == SINGLE_DATA_RESPONSE) {
            Object responseModel = model.get(responseData.iterator().next());
            responseBody(responseModel, response);
            return;
        }
        responseBody(model, response);
    }

    private void responseBody(Object responseModel, HttpServletResponse response) {
        try {
            response.setContentType(JSON_CONTENT_TYPE);
            String responseBody = objectMapper.valueToTree(responseModel).toPrettyString();
            response.getWriter().write(responseBody);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
