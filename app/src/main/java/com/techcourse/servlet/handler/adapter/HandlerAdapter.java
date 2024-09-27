package com.techcourse.servlet.handler.adapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.NoSuchElementException;

public class HandlerAdapter {

    public ModelAndView adaptHandler(
            Object handler,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {
        if (handler instanceof Controller controller) {
            return adaptController(request, response, controller);
        }

        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.handle(request, response);
        }

        throw new NoSuchElementException(request + "에 해당하는 핸들러를 찾을 수 없습니다");
    }

    private ModelAndView adaptController(
            HttpServletRequest request,
            HttpServletResponse response,
            Controller controller
    ) throws Exception {
        String viewName = controller.execute(request, response);
        JspView view = new JspView(viewName);
        return new ModelAndView(view);
    }
}
