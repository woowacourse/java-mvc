package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.adaptor.HandlerAdaptorScanner;
import webmvc.org.springframework.web.servlet.handler.HandlerScanner;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerAdaptorScanner handlerAdaptorScanner;
    private HandlerScanner handlerScanner;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerScanner = new HandlerScanner("com");
        handlerAdaptorScanner = new HandlerAdaptorScanner();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final var controller = handlerScanner.getHandler(request);
            final var handlerAdaptor = handlerAdaptorScanner.getHandlerAdaptor(controller);
            final var modelAndView = handlerAdaptor.handle(controller, request, response);
            modelAndView.render(request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
