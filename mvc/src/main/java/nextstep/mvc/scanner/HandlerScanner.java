package nextstep.mvc.scanner;

import nextstep.mvc.exeption.HandlerMappingException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HandlerScanner {

    private static final Logger log = LoggerFactory.getLogger(HandlerScanner.class);

    private final Reflections reflections;

    public HandlerScanner(Object... basePath) {
        this(new Reflections(basePath));
    }

    public HandlerScanner(Reflections reflections) {
        this.reflections = reflections;
    }

    public Map<Class<?>, Object> getHandler() {
        final Set<Class<?>> typesAnnotatedWith = reflections.getTypesAnnotatedWith(Controller.class);
        return instantiateControllers(typesAnnotatedWith);
    }

    private Map<Class<?>, Object> instantiateControllers(Set<Class<?>> typesAnnotatedWith) {
        Map<Class<?>, Object> handlers = new HashMap<>();
        try {
            for (Class<?> clazz : typesAnnotatedWith) {
                log.info(clazz.getName());
                final Object handler = clazz.getConstructor().newInstance();
                handlers.put(clazz, handler);
            }
        } catch (InvocationTargetException | InstantiationException |
                IllegalAccessException | NoSuchMethodException e) {
            log.info("핸들러에 기본생성자가 없습니다. ");
            throw new HandlerMappingException("핸들러에 기본생성자가 없습니다. ");
        }
        log.info("Scanned Handlers!");
        return handlers;
    }
}
