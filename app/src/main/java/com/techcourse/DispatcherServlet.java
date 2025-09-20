package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
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

    public DispatcherServlet() {
        handlerMappings.add(new AnnotationHandlerMapping("com/techcourse/controller"));
        handlerMappings.add(new ManualHandlerMapping());
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
            for (HandlerMapping handlerMapping : handlerMappings){
                var handler = handlerMapping.getHandler(request);
                if(handler == null){
                    continue;
                }
                HandlerAdapter.handle(request, response, handler);
                return;
            }
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
