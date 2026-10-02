package com.interface21.webmvc.servlet.view;

import com.interface21.webmvc.servlet.View;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JspView implements View {

    private static final Logger log = LoggerFactory.getLogger(JspView.class);

    public static final String REDIRECT_PREFIX = "redirect:";

    private final ViewName viewName;

    public JspView(final ViewName viewName) {
        this.viewName = viewName;
    }

    public JspView(final String viewName) {
        this(new ViewName(viewName));
    }

    @Override
    public void render(final Map<String, ?> model, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.redirectable()) {
            response.sendRedirect(viewName.path());
            return;
        }

        model.forEach((key, value) -> {
            log.debug("attribute name : {}, value : {}", key, value);
            request.setAttribute(key, value);
        });

        request.getRequestDispatcher(viewName.path()).forward(request, response);
    }
}
