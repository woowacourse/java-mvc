package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private HandlerMappings handlerMappings;
    private HandlerExecutors handlerExecutors;

    public DispatcherServlet(final Object... basePackage) {
        final AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackage);
        handlerMappings = new HandlerMappings(annotationHandlerMapping);
    }

    @Override
    public void init() {
        handlerExecutors = new HandlerExecutors(List.of(new AnnotationHandlerExecutor()));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.getHandler(request);
            final ModelAndView modelAndView = (ModelAndView) handlerExecutors.execute(handler, request, response);

            final View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
