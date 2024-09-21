package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;

public final class ComponentScanner {

	public static Map<HandlerKey, HandlerExecution> scan() {
		Reflections reflections = new Reflections(new ConfigurationBuilder()
			.forPackage("com.interface21.webmvc")
			.addScanners(Scanners.TypesAnnotated));

		Map<HandlerKey, HandlerExecution> executions = new HashMap<>();
		for (Class<?> clazz : reflections.getTypesAnnotatedWith(Controller.class)) {
			scanRequestMappingForMethods(executions, clazz);
		}
		return executions;
	}

	private static void scanRequestMappingForMethods(Map<HandlerKey, HandlerExecution> executions, Class clazz) {
		for (Method method : clazz.getDeclaredMethods()) {
			if(!method.isAnnotationPresent(RequestMapping.class)) {
				return;
			}
			String value = method.getAnnotation(RequestMapping.class).value();
			extractMethodOfRequestMapping(executions, Class clazz, value, method);
		}
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
