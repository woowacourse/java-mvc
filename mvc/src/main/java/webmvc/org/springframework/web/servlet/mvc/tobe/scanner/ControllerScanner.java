package webmvc.org.springframework.web.servlet.mvc.tobe.scanner;

import context.org.springframework.stereotype.Controller;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webmvc.org.springframework.web.servlet.exception.ControllerScannerException;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ControllerScanner {

    private static final Logger log = LoggerFactory.getLogger(ControllerScanner.class);

    public Map<Class<?>, Object> getControllers(final Object... basePackage) {
        return new Reflections(basePackage)
                .getTypesAnnotatedWith(Controller.class)
                .stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        this::getControllerInstance
                ));
    }

    private Object getControllerInstance(final Class<?> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            log.warn("Controller 를 생성자로 생성하던 도중 예외가 발생하였습니다.", e);
            throw new ControllerScannerException("[ERROR] Controller 를 생성자로 생성하던 도중 예외가 발생하였습니다.");
        }
    }
}
