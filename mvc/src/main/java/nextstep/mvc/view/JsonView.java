package nextstep.mvc.view;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.MvcException;
import nextstep.web.support.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) {
        String modelAsJson = processToJson(model);
        printToResponse(response, modelAsJson);
    }

    private String processToJson(Map<String, ?> model) {
        try {
            mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            if (model.size() == 1) {
                return mapper.writeValueAsString(model.values().iterator().next());
            }
            return mapper.writeValueAsString(model);
        } catch (JsonProcessingException exception) {
            throw new MvcException(
                    String.format("Exception when converting [%s] to json.", model),
                    exception
            );
        }
    }

    private void printToResponse(HttpServletResponse response, String modelAsJson) {
        try {
            response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
            PrintWriter writer = response.getWriter();
            writer.print(modelAsJson);
            writer.flush();
        } catch (IOException exception) {
            throw new MvcException(
                    String.format("Exception when printing \"%s\" as json to response", modelAsJson),
                    exception
            );
        }
    }
}
