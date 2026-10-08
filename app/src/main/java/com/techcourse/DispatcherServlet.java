package com.techcourse;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.HandlerMapping;
import com.interface21.webmvc.servlet.HandlerScanner;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerScanner scanner;
    private List<HandlerMapping> handlerMappings;
    private List<HandlerAdapter> handlerAdapters;

    public DispatcherServlet(final HandlerScanner scanner) {
        this.scanner = Objects.requireNonNull(scanner, "HandlerScanner는 null일 수 없습니다.");
    }

    @Override
    public void init() throws ServletException {
        try {
            handlerMappings = scanner.getHandlerMappings();
            handlerAdapters = scanner.getHandlerAdapters();
        } catch (RuntimeException e) {
            throw new ServletException("핸들러 초기화에 실패했습니다.", e);
        }

        if (handlerMappings == null || handlerMappings.isEmpty()) {
            throw new ServletException("등록된 HandlerMapping이 없습니다. 핸들러 검색 패키지를 확인해주세요.");
        }
        if (handlerAdapters == null || handlerAdapters.isEmpty()) {
            throw new ServletException("등록된 HandlerAdapter가 없습니다. 핸들러 검색 패키지를 확인해주세요.");
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = getHandler(request);
            if (handler == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            final HandlerAdapter handlerAdapter = getHandlerAdapter(handler);
            final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
            render(modelAndView, request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }

    private Object getHandler(final HttpServletRequest request) {
        return handlerMappings.stream()
                .map(mapping -> mapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private HandlerAdapter getHandlerAdapter(final Object handler) throws ServletException {
       return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findFirst()
                .orElseThrow(() -> new ServletException("지원하는 어댑터가 없습니다: " + handler.getClass().getName()));
    }

    private void render(final ModelAndView modelAndView, final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        if (modelAndView == null) {
            return;
        }
        final var view = modelAndView.getView();
        if (view == null) {
            throw new ServletException("렌더링할 View가 없습니다.");
        }
        view.render(modelAndView.getModel(), request, response);
    }
}
