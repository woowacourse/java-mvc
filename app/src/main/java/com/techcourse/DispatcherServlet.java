package com.techcourse;

import com.techcourse.support.web.mapping.HandlerMappings;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapters;

import java.util.Collections;
import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final transient HandlerMappings handlerMappings;
    private final transient HandlerAdapters handlerAdapters;

    public DispatcherServlet() {
        this.handlerMappings = new HandlerMappings();
        this.handlerAdapters = new HandlerAdapters();
    }

    @Override
    public void init() {
        handlerMappings.init();
        handlerAdapters.init();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        log.info("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final Optional<Object> handlerOptional = handlerMappings.getHandler(request);
            if (handlerOptional.isEmpty()) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final Object handler = handlerOptional.get();
            final HandlerAdapter handlerAdapter = handlerAdapters.getAdapter(handler)
                    .orElseThrow(() -> new IllegalStateException("핸들러 어댑터를 찾을 수 없습니다."));
            final ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            modelAndView.getView().render(Collections.EMPTY_MAP, request, response);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
