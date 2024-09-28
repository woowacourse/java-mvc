package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        handlerMappings.add(new ManualHandlerMapping());
        handlerMappings.add(new AnnotationHandlerMapping("com"));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object handler = getHandler(request);

            if (handler instanceof Controller controller) {
                String viewName = controller.execute(request, response); // model을 request에 담음
                JspView jspView = new JspView(viewName);
                jspView.render(new HashMap<>(), request, response); // req에 있는 model 뽑아서 view에 넣음
            }
            if (handler instanceof HandlerExecution handlerExecution) {
                ModelAndView modelAndView = handlerExecution.handle(request, response);
                View view = modelAndView.getView();
                view.render(modelAndView.getModel(), request, response);
            }

        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        throw new NoSuchElementException("Resource not found");
    }
}
