package com.techcourse.controller;

import com.interface21.context.stereotype.Controller;
import com.interface21.web.bind.annotation.RequestMapping;
import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class AnnotationRegisterController {

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView save(HttpServletRequest req, HttpServletResponse res) {
		System.out.println("AnnotationRegisterController.save");
		return null;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView show(HttpServletRequest req, HttpServletResponse res) {
		System.out.println("AnnotationRegisterController.show");
		return null;
	}
}
