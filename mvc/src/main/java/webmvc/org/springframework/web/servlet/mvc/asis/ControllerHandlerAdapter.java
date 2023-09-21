package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ControllerHandlerAdapter implements HandlerAdapter {

    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String path = ((Controller) handler).execute(request, response);
            return new ModelAndView(new JspView(path));
        } catch (Exception e) {
            throw new IllegalStateException("Controller 처리 과정에서 오류가 발생했습니다.");
        }
    }
}
