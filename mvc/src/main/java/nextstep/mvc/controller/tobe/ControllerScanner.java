package nextstep.mvc.controller.tobe;

import nextstep.mvc.controller.exception.HandlerMappingInitiationException;
import nextstep.web.annotation.Controller;
import org.reflections.Reflections;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public class ControllerScanner {
    private final Reflections reflections;

    public ControllerScanner(Object packageName) {
        this.reflections = new Reflections(packageName);
    }

    public List<Object> createInstances() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        return reflections.getTypesAnnotatedWith(Controller.class)
                .stream()
                .map(clazz -> {
                    try {
                        return clazz.getDeclaredConstructor().newInstance();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
                        exception.printStackTrace();
                        throw new HandlerMappingInitiationException(exception);
                    }
                })
                .collect(Collectors.toList());
    }
}
