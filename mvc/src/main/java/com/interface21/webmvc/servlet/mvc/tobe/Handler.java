package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class Handler {

    private Object handler;

    public Handler(Object handler) {
        this.handler = handler;
    }

    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if(handler instanceof HandlerExecution){
            HandlerExecution execution = (HandlerExecution) handler;
            ModelAndView modelAndView = execution.handle(request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
            return;
        }
        if (handler instanceof Controller) {
            Controller controller = (Controller) handler;
            String viewName = controller.execute(request, response);
            View view = new JspView(viewName);
            view.render(new HashMap<>(), request, response);
            return;
        }
    }
}
