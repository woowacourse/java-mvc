package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.adaptor.HandlerAdaptors;
import webmvc.org.springframework.web.servlet.mvc.adaptor.HandlerMappings;
import webmvc.org.springframework.web.servlet.view.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdaptors handlerAdaptors;
    private HandlerMappings handlerMappings;
    private final Object[] basePackage;

    public DispatcherServlet(final Object... basePackage) {
        this.basePackage = basePackage;
    }

    @Override
    public void init() {
        handlerAdaptors = new HandlerAdaptors();
        handlerMappings = new HandlerMappings(basePackage);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappings.getHandler(request);
            final ModelAndView modelAndView = handlerAdaptors.execute(handler, request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
