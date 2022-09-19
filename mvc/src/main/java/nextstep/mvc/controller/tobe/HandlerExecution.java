package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HandlerExecution {

    private Object[] basePackage;
    public HandlerExecution(Object[] basePackage) {
        this.basePackage = basePackage;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Reflections reflections = new Reflections(basePackage);
        List<Method> extractedMethods = reflections.getTypesAnnotatedWith(Controller.class).stream()
                .flatMap(each -> Arrays.stream(each.getMethods())
                        .filter(temp -> temp.isAnnotationPresent(RequestMapping.class)))
                .collect(Collectors.toUnmodifiableList());
        Method method = extractedMethods.stream()
                .filter(eachMethod ->
                        Arrays.stream(eachMethod.getAnnotation(RequestMapping.class).method())
                                .anyMatch(each -> each == RequestMethod.valueOf(request.getMethod()))
                                && eachMethod.getAnnotation(RequestMapping.class).value().equals(request.getRequestURI()))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return invoke(method, request, response);
    }

    private ModelAndView invoke(Method method, HttpServletRequest request, HttpServletResponse response ) {
        try {
            Class<?> declaringClass = method.getDeclaringClass();
            Object instance = declaringClass.getConstructor().newInstance();
            return (ModelAndView) method.invoke(instance, request, response);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }
}
