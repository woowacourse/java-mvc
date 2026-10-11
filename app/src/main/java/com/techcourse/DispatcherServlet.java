package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.ManualHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        handlerMappings = List.of(new AnnotationHandlerMapping("com/techcourse/controller"),
                new ManualHandlerMapping());
        handlerMappings.forEach(HandlerMapping::initialize);
        handlerAdapters = List.of(new AnnotationHandlerAdapter(), new ManualHandlerAdapter());
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            // 1. 요청을 처리할 핸들러 탐색
            final var handler = getHandler(request);
            // 2. 핸들러를 실행할 수 있는 어댑터 탐색
            final var adapter = getHandlerAdapter(handler);
            // 3. 어댑터가 핸들러를 실행하고 ModelAndView 로 통일해 반환
            final ModelAndView modelAndView = adapter.handle(request, response, handler);
            // 4. 뷰 렌더링 (forward / redirect)
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    // 핸들러를 반환하는 첫 번째 매핑의 결과를 사용
    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("처리할 핸들러가 없습니다: " + request.getRequestURI()));
    }

    // 핸들러를 지원하는 첫 번째 어댑터를 사용
    private HandlerAdapter getHandlerAdapter(final Object handler) {
        return handlerAdapters.stream()
                .filter(adapter -> adapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("지원하는 어댑터가 없습니다: " + handler));
    }
}
