package com.interface21.webmvc.servlet;

import com.interface21.webmvc.servlet.mvc.tobe.mapping.NoHandlerFoundException;
import com.interface21.webmvc.servlet.mvc.tobe.util.PropertiesLoader;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.mvc.tobe.mapping.ManualHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.AnnotationHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.ControllerHandlerAdapter;
import com.interface21.webmvc.servlet.mvc.tobe.adapter.HandlerAdapterRegistry;
import com.interface21.webmvc.servlet.view.ModelAndView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
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

        Properties props = PropertiesLoader.load(getServletContext(), "application.properties");
        log.info("Properties 로드 완료. 총 {} 개 속성", props.size());

        // ManualHandlerMapping 등록
        registerHandlerMappings(props);

        // HandlerAdapter 등록
        handlerAdapterRegistry.addHandlerAdapter(new ControllerHandlerAdapter());
        handlerAdapterRegistry.addHandlerAdapter(new AnnotationHandlerAdapter());

        log.info("DispatcherServlet 초기화 완료");
    }

    private void registerHandlerMappings(Properties props) {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping(props);
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(BASE_PACKAGE);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        log.info("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
        try {
            final Object handler = handlerMappingRegistry.getHandler(request);
            log.info("핸들러 조회 결과: {}", handler != null ? handler.getClass().getSimpleName() : "null");
            final var adapter = handlerAdapterRegistry.getHandlerAdapter(handler);
            final ModelAndView modelAndView = adapter.handle(request, response, handler);
            modelAndView.render(request,response);
        } catch (NoHandlerFoundException e) {
            log.warn(e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "요청하신 API를 찾을 수 없습니다.");
        } catch (Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage(), e);
        }
    }
}
