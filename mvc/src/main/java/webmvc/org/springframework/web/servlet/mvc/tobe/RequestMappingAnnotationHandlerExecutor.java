package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class RequestMappingAnnotationHandlerExecutor implements HandlerExecutor {

    @Override
    public boolean executable(Object handler) {
        if (!(handler instanceof Method)) {
            return false;
        }
        Method handlerMethod = (Method) handler;
        RequestMapping declaredAnnotation = handlerMethod.getDeclaredAnnotation(RequestMapping.class);
        return declaredAnnotation != null;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Method handlerMethod = (Method) handler;
        Class<?> declaringClass = handlerMethod.getDeclaringClass();
        return (ModelAndView) handlerMethod.invoke(declaringClass.getDeclaredConstructor().newInstance(), request, response);
    }
}
