package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        log.info("invoke handlerMapping add!");
        handlerMappings.add(handlerMapping);
        handlerAdapters.add(new AnnotationAdapter());
    }

    public void addAdapterMapping(HandlerAdapter handlerAdapter) {
        log.info("invoke handlerAdapter add!");
        handlerAdapters.add(handlerAdapter);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = getHandler(request);
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            log.info("get handler adapter Success. type:{}", handlerAdapter.toString());
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.debug("mapping Error");
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws Exception {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(Exception::new);
    }

    private Object getHandler(HttpServletRequest request) throws Exception {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(Exception::new);
    }
}
