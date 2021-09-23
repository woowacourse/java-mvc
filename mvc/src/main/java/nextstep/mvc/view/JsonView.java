package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter writer = response.getWriter();

        if (model.size() <= 1) {
            Object firstValue = model.values().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("표시할 값이 없습니다."));
            String jsonValue = objectMapper.writeValueAsString(firstValue);
            writer.write(jsonValue);
            return;
        }
        String value = objectMapper.writeValueAsString(model);
        writer.write(value);
    }
}
