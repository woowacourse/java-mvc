package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import nextstep.mvc.view.ModelAndView;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

/**
 * Controller 클래스의 메소드를 추상화합니다.
 */
public class ControllerMethod {

    private final Object instance;
    private final Method method;

    private ControllerMethod(final Object instance, final Method method) {
        this.instance = instance;
        this.method = method;
    }

    public static List<ControllerMethod> from(final ControllerClass controllerClass) {
        Object instance = controllerClass.newInstance();
        List<Method> methods = controllerClass.getDeclaredMethods();

        return methods.stream()
                .filter(ControllerMethod::isRequestMapping)
                .map(method -> new ControllerMethod(instance, method))
                .collect(Collectors.toList());
    }

    private static boolean isRequestMapping(final Method it) {
        return it.getAnnotation(RequestMapping.class) != null;
    }

    public ModelAndView execute(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(instance, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException();
            // TODO: 적절한 예외로 변경
        }
    }

    public String getUrl() {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return annotation.value();
    }

    public List<RequestMethod> getRequestMethods() {
        RequestMapping annotation = method.getAnnotation(RequestMapping.class);
        return Arrays.stream(annotation.method())
                .collect(Collectors.toList());
    }
}
