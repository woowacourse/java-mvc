package com.techcourse;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.tobe.Handler;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;

public class ManualHandlerMapping {

	private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

	private static final Map<String, Handler> Handlers = new HashMap<>();

	public void initialize() {
		Handlers.put("/", new ManualHandler(new ForwardController("/index.jsp")));
		Handlers.put("/login", new ManualHandler(new LoginController()));
		Handlers.put("/login/view", new ManualHandler(new LoginViewController()));
		Handlers.put("/logout", new ManualHandler(new LogoutController()));
		Handlers.put("/register/view", new ManualHandler(new RegisterViewController()));
		Handlers.put("/register", new ManualHandler(new RegisterController()));

		log.info("Initialized Handler Mapping!");
		Handlers.keySet()
			.forEach(path -> log.info("Path : {}, Controller : {}", path, Handlers.get(path).getClass()));
	}

	public Handler getHandler(final String requestURI) {
		log.debug("Request Mapping Uri : {}", requestURI);
		return Handlers.get(requestURI);
	}
}
