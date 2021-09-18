package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.exception.HandlerAdapterNotFoundException;
import nextstep.mvc.exception.HandlerMappingNotFoundException;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.JspViewException;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        handlerMappings = new ArrayList<>();
        handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = findHandler(request);
            final ModelAndView modelAndView = handleRequest(request, response, handler);
            modelAndView.render(request, response);
        } catch (HandlerMappingNotFoundException e) {
            log.error("Handler Not Found : {}", e.getMessage(), e);
            JspView.renderException(request, response, JspViewException.NOT_FOUND);
        } catch (Exception e) {
            log.error("Internal Server Error : {}", e.getMessage(), e);
            JspView.renderException(request, response, JspViewException.INTERNAL_SERVER_ERROR);
        }
    }

    private Object findHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            final Object handler = handlerMapping.getHandler(request);
            if (!Objects.isNull(handler)) {
                return handler;
            }
        }
        throw new HandlerMappingNotFoundException();
    }

    private ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response, Object handler) throws InvocationTargetException, IllegalAccessException {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter.handle(request, response, handler);
            }
        }
        throw new HandlerAdapterNotFoundException();
    }
}
