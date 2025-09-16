package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.Set;
import org.reflections.Reflections;

public class ControllerScanner {

    public Set<Class<?>> scan(Object... basePackage){
        Reflections reflections = new Reflections(basePackage);
        return reflections.getTypesAnnotatedWith(Controller.class);
    }

}
