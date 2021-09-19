package nextstep.mvc.controller.tobe;

import com.fasterxml.jackson.databind.util.ObjectBuffer;
import jakarta.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import nextstep.mvc.HandlerMapping;
import nextstep.mvc.controller.exception.HandleKeyDuplicationException;
import nextstep.web.annotation.Autowired;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger LOG = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);
        ComponentScanner componentScanner = getComponentScanner(reflections);
        initHandleExecution(reflections, componentScanner);
        initAutowired(componentScanner);
        LOG.info("Initialized AnnotationHandlerMapping!");
    }

    private void initHandleExecution(Reflections reflections, ComponentScanner componentScanner) {
        Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        typesAnnotatedWith.forEach(aClass -> {
            List<Method> declaredMethods = getRequestMappingMethods(aClass);
            addHandleExecution(componentScanner.getInstance(aClass), declaredMethods);
        });
    }

    private void initAutowired(ComponentScanner componentScanner) {
        Map<Class<?>, Object> beans = componentScanner.getBeans();

        for (Entry<Class<?>, Object> entry : beans.entrySet()) {
            Set<Field> fields = ReflectionUtils.getAllFields(
                entry.getKey(),
                ReflectionUtils.withAnnotation(Autowired.class)
            );
            setFields(componentScanner, entry.getValue(), fields);
        }
    }

    private void setFields(ComponentScanner componentScanner, Object instance,
        Set<Field> fields) {
        for (Field field : fields) {
            Class<?> fieldDeclaringClass = field.getType();
            Object value = componentScanner.getInstance(fieldDeclaringClass);
            try {
                field.setAccessible(true);
                field.set(instance, value);
            } catch (Exception e) {
                LOG.error("error : {}", e.getMessage());
            }
        }
    }

    private List<Method> getRequestMappingMethods(Class<?> aClass) {
        Method[] methods = aClass.getDeclaredMethods();
        return Arrays.stream(methods)
            .filter(method -> method.isAnnotationPresent(RequestMapping.class))
            .collect(Collectors.toList());
    }

    private void addHandleExecution(Object instance, List<Method> methods) {
        for (Method aMethod : methods) {
            RequestMapping requestMapping = aMethod.getAnnotation(RequestMapping.class);
            String url = requestMapping.value();
            for (RequestMethod requestMethod : requestMapping.method()) {
                HandlerKey handlerKey = new HandlerKey(url, requestMethod);
                validateHandleKeyDuplicate(handlerKey);
                handlerExecutions.put(handlerKey, new HandlerExecution(instance, aMethod));
                LOG.info("Request Mapping Uri : {}", url);
            }
        }
    }

    private void validateHandleKeyDuplicate(HandlerKey handlerKey) {
        if (handlerExecutions.containsKey(handlerKey)) {
            throw new HandleKeyDuplicationException();
        }
    }

    public Object getHandler(HttpServletRequest request) {
        return handlerExecutions.get(HandlerKey.of(request));
    }

    private ComponentScanner getComponentScanner(Reflections reflections) {
        ComponentScanner componentScanner = new ComponentScanner(reflections);
        componentScanner.findComponent();
        return componentScanner;
    }
}
