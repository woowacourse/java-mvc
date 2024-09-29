package com.techcourse.handlermapping;

import java.util.ArrayList;
import java.util.List;

import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.ManualHandlerMapping;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingRegistry {

	private final List<HandlerMapping> handlerMappings;

	public HandlerMappingRegistry() {
		this.handlerMappings = new ArrayList<>();

		ManualHandlerMapping manualHandlerMapping = new ManualHandlerMapping();
		manualHandlerMapping.initialize();

		this.handlerMappings.add(manualHandlerMapping);
		this.handlerMappings.add(new AnnotationHandlerMapping("com.techcourse"));
	}

	public HandlerMapping getHandlerMapping(HttpServletRequest request) {

		return handlerMappings.stream()
			.filter(hm -> hm.getHandler(request) != null)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("No handler found for requestURI: " + request.getRequestURI()));
	}
}
