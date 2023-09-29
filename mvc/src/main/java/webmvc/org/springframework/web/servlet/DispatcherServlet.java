package webmvc.org.springframework.web.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import context.org.springframework.context.ApplicationContext;
import webmvc.org.springframework.web.servlet.view.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handler.adapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.handler.mapping.HandlerMapping;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;
    private final ApplicationContext applicationContext;

    public DispatcherServlet(final ApplicationContext applicationContext) {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
        this.applicationContext = applicationContext;
    }

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdapters();
    }

    private void initHandlerMappings() {
        final List<HandlerMapping> handlerMappingInstances = applicationContext.getBeansOfType(HandlerMapping.class)
            .stream()
            .map(HandlerMapping.class::cast)
            .peek(HandlerMapping::initialize)
            .collect(Collectors.toList());
        handlerMappings.addAll(handlerMappingInstances);
    }

    private void initHandlerAdapters() {
        final List<HandlerAdapter> handlerAdapterInstances = applicationContext.getBeansOfType(HandlerAdapter.class)
            .stream()
            .map(HandlerAdapter.class::cast)
            .collect(Collectors.toList());
        handlerAdapters.addAll(handlerAdapterInstances);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
        throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            process(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void process(final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        final Object handler = getHandler(request);
        final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
            .filter(mapping -> mapping.getHandler(request) != null)
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당하는 HandlerMapping이 없습니다."))
            .getHandler(request);
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
            .filter(adapter -> adapter.supports(handler))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당하는 HandlerAdapter가 없습니다."));
    }
}
