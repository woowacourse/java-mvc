package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappingRegistry {
	private final List<HandlerMapping> handlerMappings;

	public HandlerMappingRegistry() {
		this.handlerMappings = new ArrayList<>();
	}

	public void addHandlerMapping(HandlerMapping handlerMapping) {
		handlerMappings.add(handlerMapping);
	}

	public Optional<Object> getHandler(HttpServletRequest request) {
		for (HandlerMapping handlerMapping : handlerMappings) {
			Object handler = handlerMapping.getHandler(request);
			if (handler != null) {
				return Optional.of(handler);
			}
		}
		return Optional.empty();
	}
}
