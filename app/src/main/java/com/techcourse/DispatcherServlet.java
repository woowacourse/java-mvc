package com.techcourse;

import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;

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
        annotationHandlerMapping = new AnnotationHandlerMapping();
        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            /**
             * 레거시 코드
             */
//            final var controller = manualHandlerMapping.getHandler(requestURI);
//            final var viewName = controller.execute(request, response);
//            move(viewName, request, response);

            final HandlerExecution handler = (HandlerExecution) annotationHandlerMapping.getHandler(request);
            final ModelAndView modelAndView = handler.handle(request, response);
            final Map<String, Object> model = modelAndView.getModel();
            final View view = modelAndView.getView();
            view.render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    /**
     * 레거시 코드
     */
//    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
//        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
//            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
//            return;
//        }
//
//        final var requestDispatcher = request.getRequestDispatcher(viewName);
//        requestDispatcher.forward(request, response);
//    }
}
