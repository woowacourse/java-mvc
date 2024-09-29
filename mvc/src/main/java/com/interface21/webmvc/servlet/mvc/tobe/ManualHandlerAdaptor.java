package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean isSupported(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        Controller controller = (Controller) handler;
        try {
            String path = controller.execute(request, response);
            return new ModelAndView(new JspView(path));
        } catch (Exception e) {
            throw new IllegalStateException("해당 handler를 실행하는데 오류가 발생했습니다: " + e.getMessage());
        }
    }
}
