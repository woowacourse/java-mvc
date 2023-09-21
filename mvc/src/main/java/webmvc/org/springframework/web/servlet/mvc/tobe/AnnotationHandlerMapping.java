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
    final Reflections reflections = new Reflections(basePackages);

    final Set<Class<?>> controllerAnnotationClasses =
        reflections.getTypesAnnotatedWith(Controller.class);

    for (Class<?> controllerAnnotationClass : controllerAnnotationClasses) {
      final Object instance = getControllerInstance(controllerAnnotationClass);
      final Method[] methods = controllerAnnotationClass.getDeclaredMethods();

      Arrays.stream(methods)
          .filter(method -> method.isAnnotationPresent(RequestMapping.class))
          .forEach(method -> putHandlerExecutions(method, instance));
    }

    log.info("Initialized AnnotationHandlerMapping!");

    handlerExecutions.keySet()
        .forEach(handlerKey -> log.info("handlerKey : {}, Controller : {}", handlerKey,
            handlerExecutions.get(handlerKey)));
  }

  private Object getControllerInstance(final Class<?> controllerAnnotationClass) {
    final Object instance;
    try {
      instance = controllerAnnotationClass.getDeclaredConstructor().newInstance();
    } catch (InstantiationException
             | IllegalAccessException
             | InvocationTargetException
             | NoSuchMethodException e
    ) {
      throw new IllegalArgumentException("AnnotationHandlerMapping Reflection Exception");
    }
    return instance;
  }

  private void putHandlerExecutions(final Method method, final Object target) {
    final RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

    for (final RequestMethod requestMethod : requestMapping.method()) {
      final HandlerKey handlerKey = new HandlerKey(requestMapping.value(), requestMethod);

      handlerExecutions.put(handlerKey, new HandlerExecution(method, target));
    }
  }

  @Override
  public Object getHandler(final HttpServletRequest request) {
    final String requestURI = request.getRequestURI();
    final String method = request.getMethod();

    final HandlerKey handlerKey = new HandlerKey(requestURI, RequestMethod.valueOf(method));

    return handlerExecutions.get(handlerKey);
  }
}
