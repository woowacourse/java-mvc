package com.techcourse;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

import java.util.Optional;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        annotationHandlerMapping = new AnnotationHandlerMapping("com.techcourse"); // 기존의 코드 변경 없이 리팩토링 진행
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            String viewName = executeController(request, response);
            if (viewName == null) {
                throw new IllegalArgumentException(String.format("Not found : %s", request.getRequestURI()));
            }

            move(viewName, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
//            throw new ServletException(e.getMessage());
        }
    }

    private String executeController(HttpServletRequest request, HttpServletResponse response) {
        HandlerExecution handlerExecution = annotationHandlerMapping.getHandler(request);
        if (handlerExecution != null) {
            try {
                return handlerExecution.handle(request, response).getView().getViewName();
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }
        return null;

//        Controller controller = manualHandlerMapping.getHandler(request.getRequestURI());
//        if (controller == null) {
//            return null;
//        }
//        try {
//            return controller.execute(request, response);
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
