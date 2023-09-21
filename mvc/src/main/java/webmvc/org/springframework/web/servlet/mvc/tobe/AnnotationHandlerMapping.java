package webmvc.org.springframework.web.servlet.mvc.tobe;

import context.org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import web.org.springframework.web.bind.annotation.RequestMapping;
import web.org.springframework.web.bind.annotation.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackages;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackages) {
        this.basePackages = basePackages;
        this.handlerExecutions = new HashMap<>();
    }


    @Override
    public void initialize() {
        log.info("Initialized AnnotationHandlerMapping!");
        for (Object basePackage : basePackages) {
            Reflections reflections = new Reflections(basePackage);
            Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);
            addHandlerExecutions(controllerClasses);
        }
    }

    private void addHandlerExecutions(Set<Class<?>> classes) {
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getMethods();
            Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                    .forEach(method -> addHandlerExecution(clazz, method));
        }
    }

    private void addHandlerExecution(Class<?> clazz, Method method) {
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMapping.method()[0]);
        HandlerExecution handlerExecution = new HandlerExecution(createInstance(clazz), method);
        handlerExecutions.put(handlerKey, handlerExecution);
    }

    private Object createInstance(Class<?> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("클래스에 기본 생성자가 없습니다.", e);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException("클래스를 인스턴스화 할 수 없습니다.", e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("클래스의 기본 생성자에 접근할 수 없습니다.", e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException("생성자 호출 중 예외가 발생했습니다.", e);
        }
    }


    @Override
    public Object getHandler(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.get(handlerKey);
    }

    @Override
    public boolean isMatch(final HttpServletRequest request) {
        HandlerKey handlerKey = new HandlerKey(request.getRequestURI(), RequestMethod.valueOf(request.getMethod()));
        return handlerExecutions.containsKey(handlerKey);
    }
}
