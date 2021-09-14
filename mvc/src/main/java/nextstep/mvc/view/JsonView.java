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
        String bodyJson = JsonParser.writeValueAsString(model);
        response.setContentType(APPLICATION_JSON_UTF8_VALUE);

        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bodyJson.getBytes(StandardCharsets.UTF_8));
    }
}
