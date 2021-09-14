package nextstep.mvc.view;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static nextstep.web.support.MediaType.APPLICATION_JSON_UTF8_VALUE;

public class JsonView implements View {

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String bodyJson = responseToJson(model);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bodyJson.getBytes(StandardCharsets.UTF_8));
    }

    public String responseToJson(Map<String, ?> model) throws Exception {
        if (model.size() == 1) {
            for (Object value : model.values()) {
                return JsonParser.writeValueAsString(value);
            }
        }
        return JsonParser.writeValueAsString(model);
    }
}
