package nextstep.mvc.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nextstep.web.support.MediaType;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        String valueAsString;
        valueAsString = extractedJsonDataFromModel(model);
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        PrintWriter out = response.getWriter();
        out.print(valueAsString);
        out.flush();
    }

    private String extractedJsonDataFromModel(Map<String, ?> model) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        if (model.size() == 1) {
            Set<String> rawKeys = model.keySet();
            List<String> keys = new LinkedList<>(rawKeys);
            String uniqueKey = keys.get(0);
            return objectMapper.writeValueAsString(model.get(uniqueKey));
        }

        return objectMapper.writeValueAsString(model);
    }
}
