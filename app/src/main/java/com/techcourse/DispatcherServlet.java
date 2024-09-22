package com.techcourse;

import static com.interface21.webmvc.servlet.view.JspView.REDIRECT_PREFIX;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new CopyOnWriteArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.add(new ManualHandlerMapping(new ConcurrentHashMap<>()));
        handlerMappings.add(new AnnotationHandlerMapping(new ConcurrentHashMap<>()));
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Controller handler = getHandler(request);
            final var viewName = handler.execute(request, response);
            move(viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Controller getHandler(HttpServletRequest request) {
        return (Controller) handlerMappings.stream()
                .filter(handlerMapping -> handlerMapping.canHandle(request))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found handler mapping"))
                .getHandler(request);
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(REDIRECT_PREFIX.length()));
            return;
        }

        View view = resolveView(viewName);
        ModelAndView modelAndView = new ModelAndView(view);
        view.render(modelAndView.getModel(), request, response);
    }

    private View resolveView(String viewName) {
        return new JspView(viewName);
    }
}
