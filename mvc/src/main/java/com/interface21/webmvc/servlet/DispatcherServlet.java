package com.interface21.webmvc.servlet;


import com.interface21.webmvc.servlet.mvc.adapter.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.adapter.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.mapping.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        // 1) 레거시 매핑
//        ManualHandlerMapping manual = new ManualHandlerMapping();
//        manual.initialize();
//        handlerMappings.add(manual);

        // 2) 어노테이션 매핑
        AnnotationHandlerMapping anno = new AnnotationHandlerMapping("com.techcourse.controller");
        anno.initialize(); // @Controller 스캔 후 (HandlerKey -> HandlerExecution) 등록
        handlerMappings.add(anno);

        // 3) 어댑터 등록
//        handlerAdapters.add(new SimpleControllerHandlerAdapter());
        handlerAdapters.add(new HandlerExecutionHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        Object handler = getHandler(request);
        if (handler == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        HandlerAdapter adapter = getHandlerAdapter(handler);

        try {
            ModelAndView mav = adapter.handle(request, response, handler);
            if (mav == null) {
                return;
            }

            final View view = mav.getView();
            if (view == null) {
                throw new ServletException("ModelAndView has no View");
            }

            view.render(mav.getModel(), request, response);
        } catch (ServletException | IOException e) {
            throw e;
        } catch (Throwable e) {
            throw new ServletException("Request dispatch failed", e);
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping hm : handlerMappings) {
            Object handler = hm.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalStateException("No HandlerAdapter for handler type: " + handler.getClass());
    }
}
