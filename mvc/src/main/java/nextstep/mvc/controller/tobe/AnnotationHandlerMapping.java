package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

    private final Object[] basePackage;
    private final Map<HandlerKey, HandlerExecution> handlerExecutions;

    public AnnotationHandlerMapping(final Object... basePackage) {
        this.basePackage = basePackage;
        this.handlerExecutions = new HashMap<>();
    }

    /*
            Class<Junit4Test> clazz = Junit4Test.class;

        Junit4Test instance = clazz.getConstructor()
                .newInstance();

        Arrays.stream(clazz.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(MyTest.class))
                .forEach(method -> invoke(instance, method));
     */


    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        final var classes = new HashSet<Class<?>>();
        classes.addAll(reflections.getTypesAnnotatedWith(Controller.class));

        for (Class<?> aClass : classes) {
            try {
                Object controller = aClass.getConstructor().newInstance();
                Arrays.stream(aClass.getDeclaredMethods())
                        .filter(method -> method.isAnnotationPresent(RequestMapping.class))
                        .forEach(method -> {
                            log.info(method.getName());
                            RequestMapping annotation = method.getAnnotation(RequestMapping.class);
                            HandlerKey key = new HandlerKey(annotation.value(), annotation.method()[0]);
                            HandlerExecution value = new HandlerExecution();
                            handlerExecutions.put(key, value);
                        });
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        log.info("Initialized AnnotationHandlerMapping!");
    }

    public Object getHandler(final HttpServletRequest request) {
        return null;
    }
}
