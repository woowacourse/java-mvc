package com.techcourse.controller;

import static nextstep.web.support.RequestMethod.GET;
import static nextstep.web.support.RequestMethod.POST;

import com.techcourse.domain.User;
import com.techcourse.repository.InMemoryUserRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.web.annotation.RequestMapping;

@nextstep.web.annotation.Controller
public class RegisterController implements Controller {

	@RequestMapping(value = "/register", method = {GET, POST})
	public String execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		if (req.getMethod().equals(GET.name())) {
			return "redirect:/register.jsp";
		}
		final var user = new User(2,
			req.getParameter("account"),
			req.getParameter("password"),
			req.getParameter("email"));
		InMemoryUserRepository.save(user);

		return "redirect:/index.jsp";
	}
}
