package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandler implements Handler {

	private final Controller controller;

	public ManualHandler(Controller controller) {
		this.controller = controller;
	}

	@Override
	public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		try {
			String viewName = controller.execute(request, response);
			return new ModelAndView(new JspView(viewName));
		} catch (Exception e) {
			throw new ServletException(e.getMessage());
		}
	}
}
