package com.techcourse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.AnnotationHandlerMapping;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerAdapters;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerExecutionHandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.HandlerMappings;
import webmvc.org.springframework.web.servlet.view.JspView;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

	private HandlerMappings handlerMapping;
	private HandlerAdapters handlerAdapters;

	public DispatcherServlet() {
	}

	@Override
	public void init() {
		handlerMapping = new HandlerMappings(new ManualHandlerMapping(), new AnnotationHandlerMapping());
		handlerAdapters = new HandlerAdapters(new ControllerHandlerAdapter(), new HandlerExecutionHandlerAdapter());
		handlerMapping.initialize();
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws
		ServletException {
		log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());
		try {
			Object handler = handlerMapping.getHandler(request);
			HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);
			ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);
			move(modelAndView.getViewName(), request, response);
		} catch (Throwable e) {
			log.error("Exception : {}", e.getMessage(), e);
			throw new ServletException(e.getMessage());
		}
	}

	private void move(final String viewName, final HttpServletRequest request,
		final HttpServletResponse response) throws Exception {
		if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
			response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
			return;
		}

		final var requestDispatcher = request.getRequestDispatcher(viewName);
		requestDispatcher.forward(request, response);
	}
}
