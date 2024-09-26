package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletRequest;

public class AnnotationHandlerMapping implements HandlerMapping {

	private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

	private final Object[] basePackage;
	private final Map<HandlerKey, Handler> handlers;

	public AnnotationHandlerMapping(final Object... basePackage) {
		this.basePackage = basePackage;
		this.handlers = new HashMap<>();
	}

	@Override
	public Handler getHandler(final HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
		HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
		return handlers.get(handlerKey);
	}

	@Override
	public boolean canService(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
		HandlerKey handlerKey = new HandlerKey(requestURI, requestMethod);
		return handlers.containsKey(handlerKey);
	}

	@Override
	public void initialize() {
		log.info("Initializing AnnotationHandlerMapping!");
		Set<Class<?>> controllers = findControllers();
		processControllers(controllers);
		log.info("Initialized AnnotationHandlerMapping with {} handlers", handlers.size());
	}

	private Set<Class<?>> findControllers() {
		Reflections reflections = new Reflections(basePackage);
		return reflections.getTypesAnnotatedWith(Controller.class);
	}

	private void processControllers(Set<Class<?>> controllers) {
		for (Class<?> controller : controllers) {
			Object instance = createInstance(controller);
			processMethods(controller, instance);
		}
	}

	private Object createInstance(Class<?> clazz) {
		try {
			return clazz.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
		}
	}

	private void processMethods(Class<?> controller, Object instance) {
		for (Method method : controller.getDeclaredMethods()) {
			if (method.isAnnotationPresent(RequestMapping.class)) {
				processRequestMapping(method, instance);
			}
		}
	}

	private void processRequestMapping(Method method, Object instance) {
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
		String uriPattern = requestMapping.value();
		RequestMethod[] requestMethods = requestMapping.method();

		if (requestMethods.length == 0) {
			requestMethods = RequestMethod.values();
		}

		for (RequestMethod requestMethod : requestMethods) {
			registerHandler(uriPattern, requestMethod, instance, method);
		}
	}

	private void registerHandler(String uriPattern, RequestMethod requestMethod,
		Object instance, Method method) {
		HandlerKey handlerKey = new HandlerKey(uriPattern, requestMethod);
		handlers.put(handlerKey, new AnnotationHandler(new HandlerExecution(instance, method)));
	}
}
