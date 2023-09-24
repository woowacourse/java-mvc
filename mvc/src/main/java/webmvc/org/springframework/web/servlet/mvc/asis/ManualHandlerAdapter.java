package webmvc.org.springframework.web.servlet.mvc.asis;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    private static final String HANDLER_METHOD = "execute";

    @Override
    public boolean supports(Object handler) {
        return Controller.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            Class<?> clazz = handler.getClass();
            Method method = clazz.getDeclaredMethod(
                    HANDLER_METHOD,
                    HttpServletRequest.class,
                    HttpServletResponse.class
            );
            return invokeMethod(handler, method, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException("Adapter가 handle에 실패했습니다.", e);
        }
    }

    private ModelAndView invokeMethod(
            Object object,
            Method method,
            HttpServletRequest request,
            HttpServletResponse response
    )
            throws Exception {
        String viewName = (String) method.invoke(object, request, response);
        JspView jspView = new JspView(viewName);
        return new ModelAndView(jspView);
    }
}
