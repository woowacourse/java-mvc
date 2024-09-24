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

public class AnnotationHandlerMapping {

	private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerMapping.class);

	private final Object[] basePackage;
	private final Map<HandlerKey, HandlerExecution> handlerExecutions;

	public AnnotationHandlerMapping(final Object... basePackage) {
		this.basePackage = basePackage;
		this.handlerExecutions = new HashMap<>();
	}

	public void initialize() throws NoSuchMethodException {
		log.info("Initializing AnnotationHandlerMapping!");
		Set<Class<?>> controllers = findControllers();
		processControllers(controllers);
		log.info("Initialized AnnotationHandlerMapping with {} handlers", handlerExecutions.size());
	}

	private Set<Class<?>> findControllers() {
		Reflections reflections = new Reflections(basePackage);
		return reflections.getTypesAnnotatedWith(Controller.class);
	}

	private void processControllers(Set<Class<?>> controllers) throws NoSuchMethodException {
		for (Class<?> controller : controllers) {
			Object instance = createInstance(controller);
			processMethods(controller, instance);
		}
	}

	private Object createInstance(Class<?> clazz) throws NoSuchMethodException {
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
		handlerExecutions.put(handlerKey, new HandlerExecution(instance, method));
	}

	public Object getHandler(final HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
		HandlerExecution handler = handlerExecutions.get(new HandlerKey(requestURI, requestMethod));
		if (handler == null) {
			String invalidRequestInfo = String.join(" ", requestMethod.name(), requestURI);
			throw new IllegalArgumentException("해당 요청을 처리할 수 있는 핸들러가 존재하지 않습니다. : " + invalidRequestInfo);
		}
		return handler;
	}
}
