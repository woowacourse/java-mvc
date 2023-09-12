package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import web.org.springframework.web.bind.annotation.RequestMapping;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Class<?> clazz = Class.forName("samples.TestController");
        Method any = Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> {
                    RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                    return Objects.nonNull(annotation) && annotation.value().equals(request.getRequestURI());
                })
                .findAny()
                .orElseThrow(IllegalArgumentException::new);

        Object obj = clazz.getDeclaredConstructor().newInstance();
        return (ModelAndView) any.invoke(obj, request, response);
    }
}
