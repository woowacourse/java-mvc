package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.util.Map;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            String redirectUrl = viewName.substring(REDIRECT_PREFIX.length());
            response.sendRedirect(redirectUrl);
            return;
        }

        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });

        String path = "/webapp/" + viewName + ".jsp";
        String fileContent = new String(readFile(path), StandardCharsets.UTF_8);

        response.getWriter().write(fileContent);
    }

    @Override
    public String getViewName() {
        return viewName;
    }

    private byte[] readFile(String path) throws IOException {
        try (final InputStream fileStream = getClass().getClassLoader().getResourceAsStream(path)) {
            if (fileStream == null) {
                throw new NoSuchFileException(String.format("No Such file : location = %s", path));
            }
            return fileStream.readAllBytes();
        }
    }
}
