package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;
import java.util.HashMap;
import java.util.Map;
import org.reflections.Reflections;

public class ControllerScanner {

  private final Reflections reflections;

  public ControllerScanner(final Object... basePackages) {
    reflections = new Reflections(basePackages);
  }

  public Map<Class<?>, Object> getControllers() {
    final Map<Class<?>, Object> controllers = new HashMap<>();
    for (Class<?> controllerClass : reflections.getTypesAnnotatedWith(Controller.class)) {
      controllers.put(controllerClass, createController(controllerClass));
    }
    return controllers;
  }

  private Object createController(final Class<?> controllerClass) {
    try {
      return controllerClass.getDeclaredConstructor().newInstance();
    } catch (ReflectiveOperationException exception) {
      throw new IllegalStateException(
          "Failed to create controller: " + controllerClass.getName(), exception);
    }
  }
}
