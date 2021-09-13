package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            for (HandlerMapping handlerMapping : handlerMappings) {
                if (handlerMapping.canHandle(request)) {
                    handlerMapping.handle(request, response);
                    return;
                }
            }
            throw new IllegalArgumentException("해당 요청을 처리할 핸들러가 없습니다.");
        } catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException | IOException | ServletException e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
