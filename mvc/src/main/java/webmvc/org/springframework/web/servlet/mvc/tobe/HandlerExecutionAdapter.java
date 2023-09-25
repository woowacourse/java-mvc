package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handlermapping.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(
        Object handler,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {
        Object handledResult = ((HandlerExecution) handler).handle(request, response);
        Class<?> returnType = ((HandlerExecution) handler).getReturnType();
        if (returnType == ModelAndView.class) {
            return (ModelAndView) handledResult;
        }
        if (returnType == String.class) {
            return new ModelAndView(new JspView((String) handledResult));
        }
        throw new IllegalArgumentException("지원하지 않는 반환 타입입니다. " + returnType.getSimpleName());
    }
}
