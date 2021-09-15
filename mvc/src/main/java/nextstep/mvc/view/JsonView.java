package nextstep.mvc.view;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.web.support.MediaType;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

public class JsonView implements View {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private String jsonData;

    public JsonView() {
        this(null);
    }

    public JsonView(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (Objects.isNull(jsonData)) {
            jsonData = convertToJsonData(model);
        }
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();
        writer.write(jsonData);
    }

    private String convertToJsonData(Map<String, ?> model) throws JsonProcessingException {
        if (model.size() == 1) {
            return OBJECT_MAPPER.writeValueAsString(model.values().iterator().next());
        }
        return OBJECT_MAPPER.writeValueAsString(model);
    }
}
