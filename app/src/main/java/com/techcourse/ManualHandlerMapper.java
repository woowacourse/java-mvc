package com.techcourse;

import com.techcourse.controller.v1.LoginOldController;
import com.techcourse.controller.v1.LoginViewOldController;
import com.techcourse.controller.v1.LogoutOldController;
import com.techcourse.controller.v1.RegisterOldController;
import com.techcourse.controller.v1.RegisterViewOldController;
import com.techcourse.repository.InMemoryUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import nextstep.context.PeanutBox;
import nextstep.mvc.handler.mapper.HandlerMapper;
import nextstep.mvc.handler.asis.Controller;
import nextstep.mvc.handler.asis.ForwardController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ManualHandlerMapper implements HandlerMapper {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapper.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        final InMemoryUserRepository userRepository = PeanutBox.INSTANCE.getPeanut(InMemoryUserRepository.class);

        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/old/login", new LoginOldController(userRepository));
        controllers.put("/old/login/view", new LoginViewOldController());
        controllers.put("/old/logout", new LogoutOldController());
        controllers.put("/old/register/view", new RegisterViewOldController(userRepository));
        controllers.put("/old/register", new RegisterOldController(userRepository));

        log.info("Initialized Handler Mapping!");
        controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path, controllers.get(path).getClass()));
    }

    @Override
    public Controller getHandler(HttpServletRequest request) {
        final String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }
}
