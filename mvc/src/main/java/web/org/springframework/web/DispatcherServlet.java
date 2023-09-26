package web.org.springframework.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings = new HandlerMappings();
    private final HandlerAdapters handlerAdapters = new HandlerAdapters();

    @Override
    public void init() {
        var annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse.controller");
        annotationHandlerMapping.initialize();
        handlerMappings.add(annotationHandlerMapping);
        handlerAdapters.add(new HandlerExecutionAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var optionalHandler = handlerMappings.findHandler(request);
            if (optionalHandler.isEmpty()) {
                move(new ModelAndView(new JspView("/404.jsp")), request, response);
            } else {
                Handler handler = optionalHandler.get();
                final var handlerAdapter = handlerAdapters.findHandlerAdapter(handler);
                final var modelAndView = handlerAdapter.handle(handler, request, response);
                move(modelAndView, request, response);
            }
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(
        final ModelAndView modelAndView,
        final HttpServletRequest request,
        final HttpServletResponse response
    ) throws Exception {
        Map<String, Object> model = modelAndView.getModel();
        View view = modelAndView.getView();
        view.render(model, request, response);
    }
}
