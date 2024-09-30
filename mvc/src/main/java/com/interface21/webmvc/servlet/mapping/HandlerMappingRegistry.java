package com.interface21.webmvc.servlet.mapping;

import java.util.ArrayList;
import java.util.List;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingRegistry {

	private final List<HandlerMapping> handlerMappings;

	public HandlerMappingRegistry(Object ... basePackages) {
		this.handlerMappings = new ArrayList<>();
		AnnotationHandlerMapping annotationHandlerMapping = new AnnotationHandlerMapping(basePackages);
		annotationHandlerMapping.initialize();
		this.handlerMappings.add(annotationHandlerMapping);
	}

	public HandlerMapping getHandlerMapping(HttpServletRequest request) {
		return handlerMappings.stream()
			.filter(hm -> hm.getHandler(request) != null)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("No handler found for requestURI: " + request.getRequestURI()));
	}
}
