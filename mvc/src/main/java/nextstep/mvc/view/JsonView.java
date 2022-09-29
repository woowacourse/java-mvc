package nextstep.mvc.view;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Object dataToRender = getDataToRender(model);
        String jsonString = objectMapper.writeValueAsString(dataToRender);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(jsonString);
    }

    private Object getDataToRender(Map<String,?> model) {
        if (model.size() == 1) {
            return model.values().stream()
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("출력할 데이터가 존재하지 않습니다."));
        }
        return model;
    }
}
