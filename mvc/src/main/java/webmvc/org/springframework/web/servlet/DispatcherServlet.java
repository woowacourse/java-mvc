package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.mvc.handermapping.HandlerMappings;
import webmvc.org.springframework.web.servlet.mvc.handlerAdaptor.HandlerAdaptors;
import webmvc.org.springframework.web.servlet.mvc.view.ModelAndView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final String rootPackagePath;
    private HandlerAdaptors handlerAdaptors;
    private HandlerMappings handlerMappings;

    public DispatcherServlet(final String rootPackagePath) {
        this.rootPackagePath = rootPackagePath;
    }

    @Override
    public void init() {
        handlerAdaptors = new HandlerAdaptors();
        handlerMappings = new HandlerMappings(rootPackagePath);
        handlerMappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var handler = handlerMappings.getHandler(request);
            final var modelAndView = handlerAdaptors.execute(handler, request, response);
            render(modelAndView, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        final var view = modelAndView.getView();
        final var model = modelAndView.getModel();

        view.render(model, request, response);
    }
}
