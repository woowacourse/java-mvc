package com.interface21.webmvc.servlet.mvc.handlerAdapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isSupported(Object object) {
        return object instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object object, HttpServletRequest request, HttpServletResponse response) {
        Controller controller = (Controller) object;
        try {
            String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (Exception e) {
            throw new IllegalStateException("핸들러의 요청을 처리하던 중 예외가 발생했습니다: " + e.getMessage());
        }
    }
}
