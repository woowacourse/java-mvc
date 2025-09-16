package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.interface21.webmvc.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
    private static final String APPLICATION_BASE_PACKAGE = "com.techcourse";

    private ManualHandlerMapping manualHandlerMapping;
    private AnnotationHandlerMapping annotationHandlerMapping;

    public DispatcherServlet() {
    }

    @Override
    public void init() {
        manualHandlerMapping = new ManualHandlerMapping();
        manualHandlerMapping.initialize();

        annotationHandlerMapping = new AnnotationHandlerMapping(APPLICATION_BASE_PACKAGE);
        annotationHandlerMapping.initialize();
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            if(manualHandlerMapping.containsHandlerByRequestURI(requestURI)){
                final var controller = manualHandlerMapping.getHandler(requestURI);
                final var viewName = controller.execute(request, response);
                move(viewName, request, response);
                return;
            }
            if(annotationHandlerMapping.containsHandlerByHttpServletRequest(request)){
                HandlerExecution handler = (HandlerExecution) annotationHandlerMapping.getHandler(request);
                ModelAndView modelAndView = handler.handle(request, response);
                View view = modelAndView.getView();
                view.render(modelAndView.getModel(), request, response);
            }

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void move(final String viewName, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        // PREFIX에 redirect prefix가 붙어있다면, 제거하고 리다이렉트한다.
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final var requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
