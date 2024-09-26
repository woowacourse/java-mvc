package com.techcourse.servlet;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.ModelAndView;
import com.techcourse.servlet.handlermapper.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final HandlerMappings DEFAULT_HANDLER_MAPPINGS = HandlerMappings.defaultHandlerMappings();

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappings handlerMappings;

    public DispatcherServlet() {
        this(DEFAULT_HANDLER_MAPPINGS);
    }

    public DispatcherServlet(HandlerMappings handlerMappings) {
        this.handlerMappings = handlerMappings;
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
