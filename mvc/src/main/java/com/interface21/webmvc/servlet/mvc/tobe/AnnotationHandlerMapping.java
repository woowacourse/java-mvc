package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		ControllerScanner controllerScanner = new ControllerScanner(basePackage);
		Map<Class<?>, Object> controllers = controllerScanner.getControllers();

		controllers.keySet()
			.forEach(controllerClass -> {
				Set<Method> requestMappingMethods = getRequestMappingMethods(Set.of(controllerClass));
				for (Method method : requestMappingMethods) {
					RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
					addHandlerExecutions(controllers, method, requestMapping);
				}
			});

		log.info("Initialized AnnotationHandlerMapping with controllers: {}", controllers.keySet());
	}

	private Set<Method> getRequestMappingMethods(Set<Class<?>> controllerClasses) {
		Set<Method> requestMappingMethods = new HashSet<>();
		for (Class<?> controllerClass : controllerClasses) {
			requestMappingMethods.addAll(ReflectionUtils.getAllMethods(controllerClass,
				ReflectionUtils.withAnnotation(RequestMapping.class)));
		}
		return requestMappingMethods;
	}

	private void addHandlerExecutions(Map<Class<?>, Object> controllers, Method method, RequestMapping requestMapping) {
		String url = requestMapping.value();
		RequestMethod[] requestMethods = requestMapping.method();

		List<HandlerKey> handlerKeys = mapHandlerKeys(url, requestMethods);
		Object controllerInstance = controllers.get(method.getDeclaringClass());

		for (HandlerKey handlerKey : handlerKeys) {
			handlerExecutions.put(handlerKey, new HandlerExecution(controllerInstance, method));
		}
	}

	private List<HandlerKey> mapHandlerKeys(String url, RequestMethod[] requestMethods) {
		List<HandlerKey> handlerKeys = new ArrayList<>();
		for (RequestMethod requestMethod : requestMethods) {
			handlerKeys.add(new HandlerKey(url, requestMethod));
		}
		return handlerKeys;
	}

	public Object getHandler(final HttpServletRequest request) {
		String url = request.getRequestURI();
		RequestMethod requestMethod = RequestMethod.from(request.getMethod());

		HandlerKey handlerKey = new HandlerKey(url, requestMethod);
		return handlerExecutions.get(handlerKey);
	}
}
