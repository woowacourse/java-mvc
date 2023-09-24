package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import context.org.springframework.stereotype.Controller;
import java.util.Set;
import java.util.stream.Collectors;
import org.reflections.Reflections;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.InstantiateControllerException;

public class ControllerScanner {

    private final Reflections reflections;

    public ControllerScanner(Object[] targetPackage) {
        this.reflections = new Reflections(targetPackage);
    }

    public Set<Object> getAnnotatedControllerClasses() {
        return reflections.getTypesAnnotatedWith(Controller.class).stream()
                .map(this::instantiateController)
                .collect(Collectors.toSet());
    }

    private Object instantiateController(Class<?> controller) {
        try {
            return controller.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new InstantiateControllerException();
        }
    }
}
