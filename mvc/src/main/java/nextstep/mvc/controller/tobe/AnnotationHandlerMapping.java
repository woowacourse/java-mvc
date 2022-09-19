package nextstep.mvc.controller.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.HandlerMapping;
import nextstep.web.annotation.Controller;
import nextstep.web.annotation.RequestMapping;
import nextstep.web.support.RequestMethod;

public class AnnotationHandlerMapping implements HandlerMapping {

	private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

	private final Object[] basePackage;
	private final Map<HandlerKey, HandlerExecution> handlerExecutions;

	public AnnotationHandlerMapping(final Object... basePackage) {
		this.basePackage = basePackage;
		this.handlerExecutions = new HashMap<>();
	}

	public void initialize() {
		final Set<Class<?>> controllers = findControllerClass();
		findMethodsByController(controllers);
		log.info("Initialized AnnotationHandlerMapping!");
	}

	private Set<Class<?>> findControllerClass() {
		Reflections reflections = new Reflections(basePackage);
		final Set<Class<?>> controllers = reflections.getTypesAnnotatedWith(Controller.class);
		return controllers;
	}

	private void findMethodsByController(Set<Class<?>> controllers) {
		for (Class controller : controllers) {
			final Method[] methods = controller.getDeclaredMethods();
			for (Method method : methods) {
				findMethodWithRequestMapping(controller, method);
			}
		}
	}

	private void findMethodWithRequestMapping(Class controller, Method method) {
		final Optional<RequestMapping> requestMapping = findByRequestMapping(method);
		if (requestMapping.isPresent()) {
			final RequestMapping requestMappingValue = requestMapping.get();
			setHandlerExecutions(controller, method, requestMappingValue);
		}
	}

	private Optional<RequestMapping> findByRequestMapping(Method method) {
		return Optional.ofNullable(method.getAnnotation(RequestMapping.class));
	}

	private void setHandlerExecutions(Class controller, Method method, RequestMapping requestMappingValue) {
		for (RequestMethod requestMethod : requestMappingValue.method()) {
			final HandlerKey handlerKey = new HandlerKey(requestMappingValue.value(), requestMethod);
			handlerExecutions.put(handlerKey, new HandlerExecution(controller, method));
		}
	}

	public Object getHandler(final HttpServletRequest request) {
		final HandlerKey handlerKey = new HandlerKey(request.getRequestURI(),
			RequestMethod.valueOf(request.getMethod()));
		return handlerExecutions.get(handlerKey);
	}
}
