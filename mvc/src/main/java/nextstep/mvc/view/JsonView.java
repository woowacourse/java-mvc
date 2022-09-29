package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonView implements View {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        final String jsonData = generateJsonData(model);

        response.getWriter().print(jsonData);
    }

    private String generateJsonData(Map<String, ?> model) throws JsonProcessingException {
        if (hasOneElement(model)) {
            final Entry<String, ?> entry = model.entrySet()
                    .stream()
                    .findAny()
                    .get();

            return objectMapper.writer().writeValueAsString(entry.getValue());
        }

         return objectMapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(model);
    }

    private boolean hasOneElement(Map<String, ?> model) {
        return model.size() == 1;
    }
}
