package com.techcourse;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import com.techcourse.controller.RegisterController;
import com.techcourse.controller.RegisterViewController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManualHandlerMapping implements HandlerMapping {

    private static final Logger log = LoggerFactory.getLogger(ManualHandlerMapping.class);

    private static final Map<String, Controller> controllers = new HashMap<>();

    @Override
    public void initialize() {
        controllers.put("/", new ForwardController("/index.jsp"));
        controllers.put("/login", new LoginController());
        controllers.put("/login/view", new LoginViewController());
        controllers.put("/logout", new LogoutController());
        controllers.put("/register/view", new RegisterViewController());
        controllers.put("/register", new RegisterController());

        log.info("Initialized ManualHandlerMapping!");
        ManualHandlerMapping.controllers.keySet()
                .forEach(path -> log.info("Path : {}, Controller : {}", path,
                        ManualHandlerMapping.controllers.get(path).getClass()));
    }

    @Override
    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            Controller controller = (Controller) getHandler(request);
            String viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (Exception e) {
            throw new IllegalArgumentException("컨트롤러 동작 수행 중 문제가 발생했습니다.", e);
        }
    }

    @Override
    public Object getHandler(final HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.debug("Request Mapping Uri : {}", requestURI);
        return controllers.get(requestURI);
    }

    @Override
    public boolean containsRequest(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return controllers.containsKey(requestURI);
    }
}
