package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String BASE_PACKAGE = "com.techcourse.controller";

    private HandlerMappingRegistry handlerMappingRegistry;
    private HandlerAdapterRegistry handlerAdapterRegistry;

    @Override
    public void init() {
        log.info("DispatcherServlet 초기화 시작");
        handlerMappingRegistry = new HandlerMappingRegistry();
        handlerAdapterRegistry = new HandlerAdapterRegistry();

        log.info("Properties 로드 시작");

        Properties props = new Properties();
        try (InputStream in = getServletContext().getResourceAsStream("/WEB-INF/classes/application.properties")) {
            if (in == null) {
                throw new IllegalStateException("application.properties 파일을 찾을 수 없습니다.");
            }
            props.load(in);
        } catch (IOException e) {
            log.error("application.properties 로드 실패", e);
            throw new RuntimeException("application.properties 로드 실패", e);
        }

        log.info("Properties 로드 완료. 총 {} 개 속성", props.size());

        // ManualHandlerMapping 등록
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        props.forEach((key, value) -> {
            String k = key.toString();
            String v = value.toString();
            if (k.startsWith("controller./")) {
                String path = k.substring("controller.".length());
                try {
                    log.info("컨트롤러 등록 시도: {} -> {}", path, v);
                    Class<?> clazz = Class.forName(v, true, Thread.currentThread().getContextClassLoader());
                    Controller controller = (Controller) clazz.getDeclaredConstructor().newInstance();
                    manualHandlerMapping.register(path, controller);
                    log.info("컨트롤러 등록 성공: {}", path);
                } catch (Exception e) {
                    log.error("컨트롤러 등록 실패: {} -> {}", path, v, e);
                    throw new RuntimeException("컨트롤러 등록 실패: " + v, e);
                }
            }
        });
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);

        // HandlerAdapter 등록
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        log.info("DispatcherServlet 초기화 완료");
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.info("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final Object handler = handlerMappingRegistry.getHandler(request);
            log.info("핸들러 조회 결과: {}", handler != null ? handler.getClass().getSimpleName() : "null");
            if (handler == null) {
                log.warn("핸들러를 찾을 수 없음: {}", requestURI);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "요청하신 API를 찾을 수 없습니다.");
                return;
            }
            final var adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(request, response, handler);
            render(modelAndView, request, response);

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }

    private void render(final ModelAndView modelAndView,
                        final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception {
        if (modelAndView == null) {
            return;
        }
        modelAndView.getView().render(modelAndView.getModel(), request, response);
    }
}
