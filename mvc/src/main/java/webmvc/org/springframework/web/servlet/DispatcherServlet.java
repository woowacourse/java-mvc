package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.View;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.supports.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.supports.adapter.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.supports.mapping.HandlerMappings;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappings mapping = new HandlerMappings();
    private final transient HandlerAdapters adapter = new HandlerAdapters();

    public DispatcherServlet addHandlerMapping(final HandlerMapping targetHandlerMapping) {
        mapping.addHandlerMapping(targetHandlerMapping);

        return this;
    }

    public DispatcherServlet addHandlerAdapter(final HandlerAdapter targetHandlerAdapter) {
        adapter.addHandlerAdapter(targetHandlerAdapter);

        return this;
    }

    @Override
    public void init() {
        mapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = mapping.getHandler(request);

            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return ;
            }

            final HandlerAdapter handlerAdapter = adapter.getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.execute(request, response, handler);

            move(modelAndView, request, response);
        } catch (final InvocationTargetException e) {
            final Throwable cause = e.getCause();

            log.warn("InvocationTargetException : {}", e.getMessage(), e);

            if (cause instanceof IllegalArgumentException) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, cause.getMessage());
            }
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);

            throw new ServletException(e.getMessage());
        }
    }

    private void move(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        final View view = modelAndView.getView();

        view.render(modelAndView.getModel(), request, response);
    }
}
