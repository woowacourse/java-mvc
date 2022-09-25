package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.mapping.HandlerMappingRepository;
import nextstep.mvc.view.ModelAndView;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRepository handlerMappingRepository;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappingRepository = new HandlerMappingRepository();
        this.handlerAdapters = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappingRepository.init();
        final Reflections adapterReflections = new Reflections("nextstep.mvc.adapter", Scanners.SubTypes);
        final Set<Class<? extends HandlerAdapter>> adapterTypes = adapterReflections.getSubTypesOf(
                HandlerAdapter.class);
        for (Class<? extends HandlerAdapter> adapterType : adapterTypes) {
            addAdapterInstances(adapterType);
        }
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRepository.add(handlerMapping);
    }

    private void addAdapterInstances(final Class<? extends HandlerAdapter> adapterType) {
        try {
            final HandlerAdapter handlerAdapter = adapterType.getDeclaredConstructor().newInstance();
            handlerAdapters.add(handlerAdapter);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappingRepository.getHandler(request);
            final HandlerAdapter handlerAdapter = getAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerAdapter getAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(IllegalArgumentException::new);
    }
}
