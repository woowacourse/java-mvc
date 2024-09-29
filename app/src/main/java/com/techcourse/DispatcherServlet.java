package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String DEFAULT_PACKAGE = "com.techcourse";

    private final HandlerMappingRegistry handlerMappingRegistry = new HandlerMappingRegistry();

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(DEFAULT_PACKAGE);
        annotationHandlerMapping.initialize();
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            HandlerMapping handler = handlerMappingRegistry.getHandler(request);
            /* HandlerAdapter 적용 후 추가 수정
            String viewName = controller.execute(request, response);
            JspView jspView = new JspView(viewName);
            jspView.render(null, request, response); // TODO: 2단계 annotationHandlerMapping 적용 후 수정*/
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }
}
