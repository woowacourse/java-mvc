package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AbstractHandlerAdapter implements HandlerAdapter {

	@Override
	public ModelAndView adapt(HandlerMapping handlerMapping, HttpServletRequest request, HttpServletResponse response) throws
		Exception {
		Object handler = handlerMapping.getHandler(request);

		if (handler instanceof Controller) {
			return ((Controller)handler).execute(request, response);
		} else if (handler instanceof HandlerExecution) {
			return  ((HandlerExecution)handler).handle(request, response);
		} else {
			throw new IllegalArgumentException("Unsupported handler type: " + handler.getClass());
		}
	}
}
