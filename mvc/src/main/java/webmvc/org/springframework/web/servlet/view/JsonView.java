package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

public class JsonView implements View {

    private static final int ONLY_ONE_DATA = 1;

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) {
        try {
            final PrintWriter printWriter = response.getWriter();
            final String responseBody = makeJsonData(model);

            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            printWriter.write(responseBody);
        } catch (final IOException e) {
            throw new IllegalArgumentException("[ERROR] Response Writer를 가져오는데 실패했습니다.");
        }
    }

    private String makeJsonData(final Map<String, ?> model) throws JsonProcessingException {
        try {
            if (hasOnlyOneData(model)) {
                final Object firstData = model.values().toArray(Object[]::new)[0];
                return objectMapper.writeValueAsString(firstData);
            }
            return objectMapper.writeValueAsString(model);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException("[ERROR] 해당 객체는 String으로 변환할 수 없습니다.");
        }
    }

    private Boolean hasOnlyOneData(final Map<String, ?> model) {
        return model.size() == ONLY_ONE_DATA;
    }
}
