package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    private static final int FIRST_DATA_INDEX = 0;
    private static final int SINGLE_DATA_SIZE = 1;

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        String responseBody = convertToJsonString(model);
        response.getWriter().write(responseBody);
    }

    private String convertToJsonString(final Map<String, ?> model) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        if (isSingleData(model)) {
            return objectMapper.writeValueAsString(model.values().toArray()[FIRST_DATA_INDEX]);
        }
        return objectMapper.writeValueAsString(model);
    }

    private boolean isSingleData(final Map<String, ?> model) {
        return model.size() == SINGLE_DATA_SIZE;
    }
}
