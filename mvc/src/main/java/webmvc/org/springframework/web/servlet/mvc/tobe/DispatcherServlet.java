package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerAdapterNotExistException;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.HandlerNotExistException;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.HandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping.HandlerMappings;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private HandlerMappings handlerMappings = new HandlerMappings();
    private HandlerAdapters handlerAdapters = new HandlerAdapters();

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request,
                           final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            final Object handler = findHandler(request);
            handleWithHandlerAndRequestAndResponse(handler, request, response);
        } catch (HandlerNotExistException | HandlerAdapterNotExistException e) {
            setNotFound(response);
        }
    }

    private Object findHandler(final HttpServletRequest request) {
        return handlerMappings.getHandler(request);
    }

    private void handleWithHandlerAndRequestAndResponse(final Object handler,
                                                        final HttpServletRequest request,
                                                        final HttpServletResponse response) throws ServletException {
        final HandlerAdapter handlerAdapter = getHandlerAdapterForHandler(handler);
        try {
            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            renderViewWithRequestAndResponse(modelAndView, request, response);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter getHandlerAdapterForHandler(final Object handler) {
        return handlerAdapters.getFirstHandleableAdapterForHandler(handler);
    }

    private void renderViewWithRequestAndResponse(final ModelAndView modelAndView,
                                                  final HttpServletRequest request,
                                                  final HttpServletResponse response) throws Exception {
        modelAndView.renderViewWithRequestAndResponse(request, response);
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        handlerAdapters.addAdapter(handlerAdapter);
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappings.addHandlerMapping(handlerMapping);
    }

    public void setNotFound(final HttpServletResponse response) {
        response.setStatus(404);
    }
}
