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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.lang.reflect.InvocationTargetException;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        HandlerMapping manualHandlerMapping = new ManualHandlerMapping();
        HandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping("samples");

        manualHandlerMapping.initialize();
        annotationHandlerMapping.initialize(); // TODO 리팩토링
        handlerMappingRegistry.addHandlerMapping(manualHandlerMapping);
        handlerMappingRegistry.addHandlerMapping(annotationHandlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            Object handler = handlerMappingRegistry.getHandler(request);

                if (handler instanceof Controller) {
                    String path = ((Controller) handler).execute(request, response);
                    JspView jspView = new JspView(path);
                    ModelAndView modelAndView = new ModelAndView(jspView);
                    move(modelAndView, request, response);
                } else if (handler instanceof HandlerExecution) {
                    ModelAndView modelAndView = ((HandlerExecution) handler).handle(request, response);
                    move(modelAndView, request, response);
                }
        } catch (InvocationTargetException | IllegalAccessException ex) {
            throw new RuntimeException("핸들러를 실행하는 도중 예외가 발생했습니다.");
        } catch (Exception ex) {
            throw new ServletException("예상치 못한 예외가 발생했습니다.");
        }
    }

    private void move(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
        View view = modelAndView.getView();
        view.render(modelAndView.getModel(), request, response);
    }
}
