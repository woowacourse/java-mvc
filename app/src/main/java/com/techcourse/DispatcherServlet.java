package com.techcourse;

import com.interface21.webmvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerAdapter> handlerAdapters;
    private final List<HandlerMapping> handlerMappingList;

    public DispatcherServlet() {
        this.handlerMappingList =
                List.of(new AnnotationHandlerMapping("com.techcourse.controller"));
        this.handlerAdapters = List.of(
                new AnnotatedHandlerAdapter()
        );
    }

    private static void redirectInternalError(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException {
        try {
            request.getRequestDispatcher("/500.jsp")
                    .forward(request, response);
        } catch (IOException ex) {
            throw new ServletException(ex.getMessage());
        }
    }

    @Override
    public void init() {
        for (HandlerMapping handlerMapping : handlerMappingList) {
            handlerMapping.initialize();
        }
    }

    @Override
    protected void service(
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws ServletException {
        try {
            doHandle(request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            redirectInternalError(request, response);
        }
    }

    private void doHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Object handler = findHandler(request);

        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (!handlerAdapter.isProcessable(handler)) {
                continue;
            }
            handlerAdapter.doHandle(handler, request, response);
            return;
        }

        throw new IllegalArgumentException("지원하지 않는 핸들러입니다..");
    }

    private Object findHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappingList) {
            Object handler = handlerMapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }

        throw new IllegalArgumentException("처리할 수 없는 요청입니다.");
    }
}
