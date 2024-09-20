package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final HandlerMappingRegistry handlerMappingRegistry;

    public DispatcherServlet() {
        handlerMappingRegistry = new HandlerMappingRegistry();
    }

    @Override
    public void init() {
        handlerMappingRegistry.initialize();
    }

    public void addHandlerMapping(HandlerMapping mapping) {
        handlerMappingRegistry.addHandlerMapping(mapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            Object controller = handlerMappingRegistry.getHandler(request)
                    .orElseThrow(() -> new IllegalArgumentException("요청에 일치하는 핸들러를 찾을 수 없습니다."));
            String viewName = controller.execute(request, response);

            View view = viewResolver(viewName);
            Map<String, Object> model = getModel(view);
            view.render(model, request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private View viewResolver(String viewName) {
        return new JspView(viewName);
    }

    private Map<String, Object> getModel(View view) {
        ModelAndView modelAndView = new ModelAndView(view);
        return modelAndView.getModel();
    }
}
