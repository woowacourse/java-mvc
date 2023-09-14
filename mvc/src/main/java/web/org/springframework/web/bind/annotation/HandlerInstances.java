package web.org.springframework.web.bind.annotation;

import org.reflections.Reflections;

public class HandlerInstances {

    private final Reflections reflections;

    public HandlerInstances(Object... basePackage) {
        this.reflections = new Reflections(basePackage);
    }


}
