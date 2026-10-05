package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.ServletContext;
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
        final ServletContext servletContext = request.getServletContext();
        try (final InputStream in = servletContext.getResourceAsStream(path)) {
            if (in == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            response.setContentType(resolveContentType(servletContext));
            in.transferTo(response.getOutputStream());
        }
    }

    private String resolveContentType(final ServletContext servletContext) {
        final String mimeType = servletContext.getMimeType(path);
        if (mimeType == null) {
            return "application/octet-stream";
        }
        if (isText(mimeType)) {
            return mimeType + ";charset=UTF-8";
        }
        return mimeType;
    }

    private boolean isText(final String mimeType) {
        return mimeType.startsWith("text/")
                || mimeType.equals("application/javascript")
                || mimeType.equals("application/json");
    }
}
