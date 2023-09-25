package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class AnnotationControllerAdapter implements HandlerAdapter {

    @Override
    public boolean support(Object object) {
        return object instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        checkValidAdapter(handler);
        return ((HandlerExecution) handler).handle(request, response);
    }

    private void checkValidAdapter(Object handler) {
        if (!(handler instanceof HandlerExecution)) {
            throw new IllegalArgumentException("해당 Adapter 는 전달된 handler 를 처리할 수 없습니다.");
        }
    }
}
