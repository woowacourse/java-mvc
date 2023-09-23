package webmvc.org.springframework.web.servlet.view;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.http.MediaType;
import webmvc.org.springframework.web.servlet.View;

import java.util.Map;
import java.util.StringJoiner;

public class JsonView implements View {

    private static final String JSON_PREFIX = "{";
    private static final String JSON_POSTFIX = "}";
    private static final String LINE_SEPARATOR = "\r\n";
    private static final String KEY_VALUE_FORMAT = "\t%s: %s";

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request,
                       HttpServletResponse response) throws Exception {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);

        ServletOutputStream outputStream = response.getOutputStream();
        String jsonContent = generateJsonFrom(model);

        outputStream.write(jsonContent.getBytes());
        response.flushBuffer();
    }

    private String generateJsonFrom(Map<String, ?> model) {
        StringJoiner stringJoiner = new StringJoiner(LINE_SEPARATOR);
        stringJoiner.add(JSON_PREFIX);
        model.forEach((key, value) -> stringJoiner.add(String.format(KEY_VALUE_FORMAT, key, value)));
        stringJoiner.add(JSON_POSTFIX);
        return stringJoiner.toString();
    }
}
