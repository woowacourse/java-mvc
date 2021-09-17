package nextstep.mvc.view;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.converter.JsonConverter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {
    
    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonConverter jsonObjectMapper = JsonConverter.getInstance();
        String json = jsonObjectMapper.convert(model);
        PrintWriter writer = response.getWriter();
        writer.write(json);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);
    }
}
