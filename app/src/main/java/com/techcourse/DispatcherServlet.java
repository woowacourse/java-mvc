package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.*;
import com.interface21.webmvc.servlet.mvc.tobe.exception.NoHandlerAdapterFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.exception.NoHandlerMappingFoundException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public DispatcherServlet() {
        handlerMappings.add(new AnnotationHandlerMapping("com.techcourse.controller"));
        handlerMappings.add(new ManualHandlerMapping());
        handlerAdapters.add(new ControllerAdapter());
        handlerAdapters.add(new HandlerExecutionAdapter());
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    /**
     * 모든 handlerMapping을 확인하며 요청을 처리할 핸들러가 있는지 찾습니다.
     * 요청을 처리할 핸들러를 찾았으면, HandlerAdapter를 통해 핸들러에 적합한 방식으로 요청을 처리합니다.
     * 적절한 핸들러를 찾을 수 없는 경우 에러 응답을 보냅니다.
     */
    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            // request를 처리할 수 있는 handlerMapping 구현체 선택, 실행
            HandlerMapping handlerMapping = getHandlerMapping(request, response);
            var handler = handlerMapping.getHandler(request);
            // handler를 지원하는 HandlerAdapter 구현체 선택, 실행
            HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            View view = modelAndView.getView();
            Map<String, Object> model = modelAndView.getModel();
            view.render(model, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private HandlerMapping getHandlerMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
        for (HandlerMapping handlerMapping : handlerMappings) {
            if (handlerMapping.support(request)) {
                return handlerMapping;
            }
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND);
        throw new NoHandlerMappingFoundException("No HandlerMapping Found!");
    }

    private HandlerAdapter getHandlerAdapter(Object handler) {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }
        throw new NoHandlerAdapterFoundException("No HandlerAdapter Found!");
    }
}
