package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import com.interface21.context.stereotype.Controller;

public class ControllerScanner {
	private final Reflections reflections;

	public ControllerScanner(Object... basePackage) {
		this.reflections = new Reflections(basePackage);
	}

	public Map<Class<?>, Object> getControllers() {
		Set<Class<?>> controllerClasses = reflections.getTypesAnnotatedWith(Controller.class);

		return instantiateControllers(controllerClasses);
	}

	private static Map<Class<?>, Object> instantiateControllers(Set<Class<?>> controllerClasses) {
		Map<Class<?>, Object> controllers = new HashMap<>();
		for (Class<?> controllerClass : controllerClasses) {
			try {
				Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
				controllers.put(controllerClass, controllerInstance);
			} catch (Exception e) {
				throw new RuntimeException("Failed to instantiate controller: " + controllerClass.getName(), e);
			}
		}
		return controllers;
	}
}
