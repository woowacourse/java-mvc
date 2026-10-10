package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.SimpleHandlerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;
    private HandlerAdapter handlerAdapter;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        AnnotationHandlerMapping annotationHandlerMapping =
                new AnnotationHandlerMapping("com.techcourse.controller");

        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();

        validateNoDuplicateMapping(manualHandlerMapping, annotationHandlerMapping);

        handlerMappings = List.of(manualHandlerMapping, annotationHandlerMapping);
        handlerAdapter = new SimpleHandlerAdapter();
    }

    // manualHandlerMapping이 handlerMappings에서 먼저 조회되기 때문에, 컨트롤러를 애노테이션 방식으로
    // 전환하면서 기존 수동 등록을 지우지 않으면 새 핸들러 대신 예전 핸들러가 예외 없이 계속 호출된다.
    // 이 상태를 그냥 넘어가지 않고 기동 시점에 바로 드러나도록 막는다.
    private void validateNoDuplicateMapping(final ManualHandlerMapping manualHandlerMapping,
                                             final AnnotationHandlerMapping annotationHandlerMapping) {
        Set<String> duplicatedPaths = new HashSet<>(manualHandlerMapping.getMappedPaths());
        duplicatedPaths.retainAll(annotationHandlerMapping.getMappedPaths());

        if (!duplicatedPaths.isEmpty()) {
            throw new IllegalStateException("수동 매핑과 애노테이션 매핑에 중복 등록된 경로가 있습니다: " + duplicatedPaths);
        }
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);
        try {
            Object handler = null;
            for (HandlerMapping handlerMapping : handlerMappings) {
                handler = handlerMapping.getHandler(request);
                if (handler != null) {
                    break;
                }
            }
            if (handler == null) {
                throw new IllegalStateException("핸들러를 찾을 수 없습니다: " + requestURI);
            }
            ModelAndView modelAndView = handlerAdapter.handle(handler, request, response);
            modelAndView.getView().render(modelAndView.getModel(), request, response);
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e);
        }
    }
}
