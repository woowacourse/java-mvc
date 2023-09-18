package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandler implements Handler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ManualHandler.class);

    private final Controller controller;

    public ManualHandler(final Controller controller) {
        this.controller = controller;
    }

    @Override
    public void handle(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        try {
            final var viewName = controller.execute(request, response);
            move(viewName, request, response);
        } catch (final Throwable e) {
            LOGGER.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
