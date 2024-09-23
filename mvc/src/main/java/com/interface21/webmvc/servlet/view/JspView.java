package com.interface21.webmvc.servlet.view;

import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.View;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";
    private static final String RESOURCE_PATH = "/META-INF/jsp/";
    private static final String FILE_EXTENSION = ".jsp";

    private final String viewName;

    public JspView(final String viewName) {
        this.viewName = viewName;
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        model.keySet().forEach(key -> {
            log.debug("attribute name : {}, value : {}", key, model.get(key));
            request.setAttribute(key, model.get(key));
        });
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }
        String jspPath = RESOURCE_PATH + viewName + FILE_EXTENSION;
        request.getRequestDispatcher(jspPath).forward(request, response);
    }
}
