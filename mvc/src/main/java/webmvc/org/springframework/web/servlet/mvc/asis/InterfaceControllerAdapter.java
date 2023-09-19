package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.view.JspView;

public class InterfaceControllerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object object) {
        return object instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        checkValidAdapter(handler);
        String responsePath = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(responsePath));
    }

    private void checkValidAdapter(Object handler) {
        if (!(handler instanceof Controller)) {
            throw new IllegalArgumentException("해당 Adapter 는 전달된 handler 를 처리할 수 없습니다.");
        }
    }
}
