package com.interface21.webmvc.servlet.view;

import com.interface21.web.http.MediaType;
import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

public class JsonView implements View {

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        String responseBody = JsonUtils.toJson(model);

        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        setResponseBody(response, responseBody);
    }

    private void setResponseBody(HttpServletResponse response, String body) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(body);
        writer.flush();
    }
}
