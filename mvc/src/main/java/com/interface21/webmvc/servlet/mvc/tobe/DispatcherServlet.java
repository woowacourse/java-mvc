package com.interface21.webmvc.servlet.mvc.tobe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

	private final List<HandlerMapping> handlerMappings;
	private final String basePackage;

	public DispatcherServlet(String basePackage) {
		this.handlerMappings = new ArrayList<>();
		this.basePackage = basePackage;
	}

	@Override
	public void init() {
		handlerMappings.add(new AnnotationHandlerMapping(new HashMap<>(), basePackage));
		handlerMappings.forEach(HandlerMapping::initialize);
	}

	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) throws
		ServletException {
		final String requestURI = request.getRequestURI();
		log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

		try {
			Handler handler = selectHandler(request);
			ModelAndView modelAndView = handler.handle(request, response);
			move(modelAndView, request, response);
		} catch (Throwable e) {
			log.error("Exception : {}", e.getMessage(), e);
			throw new ServletException(e.getMessage());
		}
	}

	private Handler selectHandler(HttpServletRequest request) {
		return handlerMappings.stream()
			.filter(handlerMapping -> handlerMapping.canService(request))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("핸들러가 없습니다."))
			.getHandler(request);
	}

	private void move(final ModelAndView modelAndView, final HttpServletRequest request,
		final HttpServletResponse response) throws Exception {
		View view = modelAndView.getView();
		view.render(modelAndView.getModel(), request, response);
	}
}
