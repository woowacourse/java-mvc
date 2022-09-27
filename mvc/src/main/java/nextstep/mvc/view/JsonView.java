package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private final ObjectMapper objectMapper;

    public JsonView() {
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final var modelToWrite = getModelToWrite(model);
        final var responseBody = objectMapper.writeValueAsString(modelToWrite);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.setContentLength(responseBody.getBytes().length);

        try (final var bufferedWriter = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bufferedWriter.write(responseBody);
            bufferedWriter.flush();
        }
    }

    private Object getModelToWrite(final Map<String, ?> model) {
        if (model.size() == 1) {
            return model.entrySet()
                    .stream()
                    .map(Entry::getValue)
                    .collect(Collectors.toList())
                    .get(0);
        }

        return model;
    }
}
