package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handlerMapping.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.handlerMapping.HandlerMappings;
import webmvc.org.springframework.web.servlet.view.View;
import webmvc.org.springframework.web.servlet.mvc.handlerAdapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handlerAdapter.HandlerAdapters;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings = new HandlerMappings();

    private final HandlerAdapters handlerAdapters = new HandlerAdapters();

    @Override
    public void init() {
        handlerMappings.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappings.findHandler(request);
            final HandlerAdapter adapter = handlerAdapters.findAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(handler, request, response);

            final View view = modelAndView.getView();
            final Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }
}
