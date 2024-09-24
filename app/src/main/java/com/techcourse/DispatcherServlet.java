package com.techcourse;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;

    public DispatcherServlet() {
        List<HandlerMapping> handlerMappings = List.of(
                new ManualHandlerMapping(),
                new AnnotationHandlerMapping()
        );
        this.handlerMappings = new HandlerMappings(handlerMappings);
    }

    @Override
    public void init() {
        handlerMappings.initialize();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            if (!handlerMappings.hasHandler(request)) {
                throw new BadRequestException("requestUrI를 처리한 핸들러가 없습니다" + request.getRequestURI());
            }

            Handler handler = handlerMappings.getHandler(request);
            ModelAndView modelAndView = handler.handle(request, response);
            modelAndView.render(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
