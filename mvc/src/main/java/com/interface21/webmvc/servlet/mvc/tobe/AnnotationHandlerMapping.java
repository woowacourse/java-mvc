package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

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

	public void initialize() {
		Stream.of(basePackage)
			.map(Reflections::new)
			.flatMap(reflection -> reflection.getTypesAnnotatedWith(Controller.class).stream())
			.forEach(this::initializeWithEachController);

		log.info("Initialized AnnotationHandlerMapping!");
	}

	private void initializeWithEachController(Class<?> controller) {
		try {
			Object instance = controller.getDeclaredConstructor().newInstance();
			Method[] methods = controller.getMethods();

			registerHandlerExecutions(instance, methods);
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
			log.error("Error instantiating controller class: {}", controller.getSimpleName(), e);
		} catch (Exception e) {
			log.error("Unexpected exception occurred: {}", controller.getSimpleName(), e);
		}
	}

	private void registerHandlerExecutions(Object instance, Method[] methods) {
		Stream.of(methods)
			.filter(method -> method.isAnnotationPresent(RequestMapping.class))
			.forEach(method -> registerHandlerExecution(instance, method));
	}

	private void registerHandlerExecution(Object instance, Method method) {
		HandlerExecution handlerExecution = new HandlerExecution(instance, method);
		RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);

		registerByRequestMapping(requestMapping, handlerExecution);
	}

	private void registerByRequestMapping(RequestMapping requestMapping, HandlerExecution handlerExecution) {
		String url = requestMapping.value();
		RequestMethod[] requestMethods = getRequestMethods(requestMapping);

		Stream.of(requestMethods)
			.map(requestMethod -> new HandlerKey(url, requestMethod))
			.forEach(handlerKey -> handlerExecutions.put(handlerKey, handlerExecution));
	}

	private RequestMethod[] getRequestMethods(final RequestMapping requestMapping) {
		RequestMethod[] methods = requestMapping.method();
		if (methods.length == 0) {
			return RequestMethod.values();
		}

		return methods;
	}

	public HandlerExecution getHandler(final HttpServletRequest request) {
		String url = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());

		HandlerKey handlerKey = new HandlerKey(url, requestMethod);
		HandlerExecution handlerExecution = handlerExecutions.get(handlerKey);

		if (handlerExecution == null) {
			log.warn("No handler found for URL: {} and Method: {}", url, requestMethod);
		}

		return handlerExecution;
	}
}
