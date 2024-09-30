package com.techcourse;

import com.interface21.exception.HandlerNotFoundException;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.ServletRequestHandler;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements ServletRequestHandler {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
    }

    public Controller getHandler(final String requestURI) {
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }

    @Override
    public boolean canHandle(HttpServletRequest request) {
        return getController(request).isPresent();
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        final var controller = getController(request)
                .orElseThrow(() -> new HandlerNotFoundException(request));
        final var viewName = controller.execute(request, response);
        render(viewName, request, response);
    }

    private Optional<Controller> getController(HttpServletRequest request) {
        return Optional.ofNullable(controllers.get(request.getRequestURI()));
    }

    private void render(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
