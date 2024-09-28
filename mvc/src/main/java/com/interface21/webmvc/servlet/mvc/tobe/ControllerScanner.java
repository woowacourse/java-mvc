package com.interface21.webmvc.servlet.mvc.tobe;

import static org.reflections.ReflectionUtils.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public final class ControllerScanner {

	public static Map<HandlerKey, HandlerExecution> scan(Object ... basePackages) {
		Reflections reflections = new Reflections(basePackages, Scanners.TypesAnnotated);

		Map<HandlerKey, HandlerExecution> executions = new HashMap<>();
		reflections.getTypesAnnotatedWith(Controller.class)
			.forEach(clazz -> scanRequestMappingForMethods(executions, clazz));
		return executions;
	}

	private static void scanRequestMappingForMethods(Map<HandlerKey, HandlerExecution> executions, Class clazz) {
		getAllMethods(clazz, withAnnotation(RequestMapping.class))
			.forEach(method -> {
				String value = method.getAnnotation(RequestMapping.class).value();
				extractMethodOfRequestMapping(executions, clazz, value, method);
			});
	}

	private static void extractMethodOfRequestMapping(Map<HandlerKey, HandlerExecution> executions, Class clazz, String value, Method method) {
		RequestMethod[] requestMethods = method.getAnnotation(RequestMapping.class).method();
		if(requestMethods.length == 0) {
			Arrays.stream(RequestMethod.values())
				.forEach(requestMethod -> executions.put(
					new HandlerKey(value, requestMethod), new HandlerExecution(clazz, method))
				);
			return;
		}
		Arrays.stream(requestMethods)
			.forEach(requestMethod -> executions.put(
				new HandlerKey(value, requestMethod), new HandlerExecution(clazz, method))
			);
	}
}
