package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.InputStream;
import java.util.Map;

public class StaticFileView implements View {

    private final String path;

    public StaticFileView(final String path) {
        this.path = path;
    }

    @Override
    public void render(final Map<String, ?> model,
                       final HttpServletRequest request,
                       final HttpServletResponse response) throws Exception {
        try (final InputStream in = request.getServletContext().getResourceAsStream(path)) {
            if (in == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setContentType("text/html;charset=UTF-8");
            in.transferTo(response.getOutputStream());
        }
    }
}
