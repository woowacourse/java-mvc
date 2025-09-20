package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecutionAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.exception.NoHandlerAdapterFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.exception.NoHandlerMappingFoundException;
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
            for (HandlerMapping handlerMapping : handlerMappings){
                if(handlerMapping.support(request)){
                    var handler = handlerMapping.getHandler(request);
                    // handler를 지원하는 핸들러 어댑터 구현체 선택, 실행
                    for (HandlerAdapter handlerAdapter : handlerAdapters) {
                        if (handlerAdapter.support(handler)) {
                            handlerAdapter.handle(request, response, handler);
                            return;
                        }
                    }
                    throw new NoHandlerAdapterFoundException("No HandlerAdapter Found!");
                }
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            throw new NoHandlerMappingFoundException("No HandlerMapping Found!");
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
