package com.techcourse;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.Handler;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerMapping;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;

import jakarta.servlet.http.HttpServletRequest;

public class ManualHandlerMapping implements HandlerMapping {

	private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

	private static final Map<String, Handler> handler = new HashMap<>();

	@Override
	public void initialize() {
		handler.put("/", new ManualHandler(new ForwardController("/index.jsp")));
		handler.put("/login", new ManualHandler(new LoginController()));
		handler.put("/login/view", new ManualHandler(new LoginViewController()));
		handler.put("/logout", new ManualHandler(new LogoutController()));
		handler.put("/register/view", new ManualHandler(new RegisterViewController()));
		handler.put("/register", new ManualHandler(new RegisterController()));

		log.info("Initialized Handler Mapping!");
		handler.keySet()
			.forEach(path -> log.info("Path : {}, Controller : {}", path, handler.get(path).getClass()));
	}

	@Override
	public Handler getHandler(HttpServletRequest request) {
		log.debug("Request Mapping Uri : {}", request.getRequestURI());
		return handler.get(request.getRequestURI());
	}

	@Override
	public boolean canService(HttpServletRequest request) {
		return handler.containsKey(request.getRequestURI());
	}
}
