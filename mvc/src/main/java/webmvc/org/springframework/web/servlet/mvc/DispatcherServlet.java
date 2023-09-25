package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;

    public DispatcherServlet(ServletContext servletContext) {
        List<HandlerMapping> handlerMappings = (List<HandlerMapping>) servletContext.getAttribute("handlerMappings");
        this.handlerMappings = new HandlerMappings(handlerMappings);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            handle(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void handle(final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final HandlerExecution handler = handlerMappings.getHandler(request);
        final ModelAndView modelAndView = handler.handle(request, response);
        modelAndView.render(request, response);
    }

}
