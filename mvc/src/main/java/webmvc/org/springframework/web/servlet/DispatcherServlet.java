package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdaptor;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecution;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private transient HandlerMappings handlerMappings;
    private transient HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        this.handlerMappings = HandlerMappings.create();
        this.handlerAdapters = HandlerAdapters.create();
        log.info("Initializing dispatcher!");
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            process(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void process(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final HandlerExecution handler = handlerMappings.getHandler(request);
        final HandlerAdaptor handlerAdapter = handlerAdapters.getHandlerAdapter(handler.method());
        final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
        final View view = modelAndView.getView();

        view.render(modelAndView.getModel(), request, response);
    }
}
