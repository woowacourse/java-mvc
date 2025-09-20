package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final HandlerMappingRegistry handlerMappingRegistry;
    private final HandlerAdapterRegistry handlerAdapterRegistry;

    public static DispatcherServlet initialize() {
        final HandlerMappingRegistry handlerMappingRegistry = HandlerMappingRegistry.initialize();
        final HandlerAdapterRegistry handlerAdapterRegistry = HandlerAdapterRegistry.initialize();
        return new DispatcherServlet(handlerMappingRegistry, handlerAdapterRegistry);
    }

    @Override
    public void init() {
        log.debug("I love immutability");
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final ModelAndView modelAndView = handlerAdapterRegistry.handle(
                    handlerMappingRegistry.getHandler(request),
                    request,
                    response);

            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (final Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
