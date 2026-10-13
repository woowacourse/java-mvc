package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 모든 요청의 진입점(Front Controller).
 * 어떤 매핑/어댑터를 쓸지는 외부에서 주입받고, 요청 처리 순서만 책임진다.
 */
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(final List<HandlerMapping> handlerMappings, final List<HandlerAdapter> handlerAdapters) {
        this.handlerMappings = List.copyOf(handlerMappings);
        this.handlerAdapters = List.copyOf(handlerAdapters);
    }

    @Override
    public void init() {
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            // 1. 요청을 처리할 핸들러 탐색
            final var handler = getHandler(request);
            // 2. 핸들러를 실행할 수 있는 어댑터 탐색
            final var adapter = getHandlerAdapter(handler);
            // 3. 어댑터가 핸들러를 실행하고 ModelAndView 로 통일해 반환
            final ModelAndView modelAndView = adapter.handle(request, response, handler);
            // 4. 뷰 렌더링 (forward / redirect / json)
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
