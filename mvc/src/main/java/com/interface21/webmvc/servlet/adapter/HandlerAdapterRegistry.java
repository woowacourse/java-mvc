package com.interface21.webmvc.servlet.adapter;

import java.util.ArrayList;
import java.util.List;

import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;

public class HandlerAdapterRegistry {

	private final List<HandlerAdapter> handlerAdapters;

	public HandlerAdapterRegistry() {
		this.handlerAdapters = new ArrayList<>();
		this.handlerAdapters.add(new ControllerAdapter());
		this.handlerAdapters.add(new HandlerExecutionAdapter());
	}

	public HandlerAdapter getHandlerAdapter(Object handler) {
		return handlerAdapters.stream()
			.filter(ha -> ha.canHandle(handler))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("No handler adapter found for handler: " + handler.getClass().getCanonicalName()));
	}
}
