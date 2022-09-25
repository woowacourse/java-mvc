package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.adapter.HandlerAdapter;
import nextstep.mvc.adapter.HandlerAdapterRepository;
import nextstep.mvc.mapping.HandlerMapping;
import nextstep.mvc.mapping.HandlerMappingRepository;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRepository handlerMappingRepository;
    private final HandlerAdapterRepository handlerAdapterRepository;

    public DispatcherServlet() {
        this.handlerMappingRepository = new HandlerMappingRepository();
        this.handlerAdapterRepository = new HandlerAdapterRepository();
    }

    @Override
    public void init() {
        handlerMappingRepository.init();
        handlerAdapterRepository.init();
    }

    public void addHandlerMapping(final HandlerMapping handlerMapping) {
        handlerMappingRepository.add(handlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            final Object handler = handlerMappingRepository.getHandler(request);
            final HandlerAdapter handlerAdapter = handlerAdapterRepository.getAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
