package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerScanner;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public void init() throws ServletException {
        try {
            ControllerScanner controllerScanner = new ControllerScanner("com.techcourse.controller");
            handlerMappings.add(new AnnotationHandlerMapping(controllerScanner));
            handlerMappings.forEach(HandlerMapping::initialize);
            handlerAdapters.add(new HandlerExecutionHandlerAdapter());

        } catch (ReflectiveOperationException exception) {
            throw new ServletException("컨트롤러 초기화에 실패했습니다.", exception);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            for (HandlerMapping handlerMapping : handlerMappings) {
                Object handler = handlerMapping.getHandler(request);
                if (handler == null) {
                    continue;
                }
                HandlerAdapter handlerAdapter = handlerAdapters.stream()
                        .filter(adapter -> adapter.supports(handler))
                        .findFirst()
                        .orElseThrow(() -> new ServletException("지원하는 어댑터를 찾을 수 없습니다."));

                ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
                modelAndView.getView().render(modelAndView.getModel(), request, response);
                return;
            }
            throw new ServletException("요청을 처리할 핸들러를 찾을 수 없습니다.");
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
