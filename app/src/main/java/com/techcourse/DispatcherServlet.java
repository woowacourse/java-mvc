package com.techcourse;

import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMappingRegistry;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);
	private static final String NOT_FOUND_VIEW_NAME = "404.jsp";

	private HandlerMappingRegistry handlerMappingRegistry;

	public DispatcherServlet() {
	}

	@Override
	public void init() {
		handlerMappingRegistry = new HandlerMappingRegistry();
	}

	public void addHandlerMapping(HandlerMapping handlerMapping) {
		handlerMappingRegistry.addHandlerMapping(handlerMapping);
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
		final String requestURI = request.getRequestURI();
		log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

		try {
			Optional<Object> handler = handlerMappingRegistry.getHandler(request);
			if (handler.isEmpty()) {
				response.setStatus(404);
				JspView notFoundView = new JspView(NOT_FOUND_VIEW_NAME);
				notFoundView.render(Map.of(), request, response);
				return;
			}
			ModelAndView modelAndView = handle(handler.get(), request, response);
			render(modelAndView, request, response);
		} catch (Throwable e) {
			log.error("Exception : {}", e.getMessage(), e);
			throw new ServletException(e.getMessage());
		}
	}

	private void render(ModelAndView modelAndView, HttpServletRequest request, HttpServletResponse response) throws Exception {
		JspView view = (JspView)modelAndView.getView();
		view.render(modelAndView.getModel(), request, response);
	}

	private ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (handler instanceof Controller) {
			String viewName = ((Controller)handler).execute(request, response);
			return new ModelAndView(new JspView(viewName));
		}
		if (handler instanceof HandlerExecution) {
			return ((HandlerExecution)handler).handle(request, response);
		}
		throw new IllegalArgumentException("Unsupported handler type: " + handler.getClass().getName());
	}
}
