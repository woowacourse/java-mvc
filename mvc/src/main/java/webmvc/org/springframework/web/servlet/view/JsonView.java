package webmvc.org.springframework.web.servlet.view;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;

import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        final PrintWriter writer = response.getWriter();

        final String jsonResult = serialize(model);
        writer.print(jsonResult);
    }

    private String serialize(final Map<String, ?> model) throws JsonProcessingException {
        if(model.isEmpty()){
            return "";
        }

        if (model.size() == 1) {
            final Object singleModel = model.values()
                    .iterator().next();
            return mapper.writeValueAsString(singleModel);
        }

        return mapper.writerWithDefaultPrettyPrinter()
                .writeValueAsString(model);
    }
}
