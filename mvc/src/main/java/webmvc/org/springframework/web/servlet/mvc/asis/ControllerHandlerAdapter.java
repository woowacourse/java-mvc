package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterException;
import webmvc.org.springframework.web.servlet.view.JspView;

import java.io.IOException;

public class ControllerHandlerAdapter implements HandlerAdapter {

    private static final String HANDLER_CLASS_NAME = ControllerHandlerAdapter.class.getCanonicalName();

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            final var controller = (Controller) handler;
            final var viewName = controller.execute(request, response);

            move(viewName, request, response);
        } catch (Exception exception) {
            throw new HandlerAdapterException("cannot adapt handler " + HANDLER_CLASS_NAME, exception);
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws IOException, ServletException {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
