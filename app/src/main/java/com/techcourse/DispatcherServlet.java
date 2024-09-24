package com.techcourse;

import com.interface21.web.bind.annotation.RequestMethod;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.HandlerKeys;
import com.interface21.webmvc.servlet.mvc.HandlerMapping;
import com.interface21.webmvc.servlet.mvc.asis.ForwardController;
import com.interface21.webmvc.servlet.mvc.asis.MapHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.AnnotationHandlerMapping;
import com.interface21.webmvc.servlet.mvc.tobe.ControllerContainer;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerKey;
import com.interface21.webmvc.servlet.view.JspView;
import com.techcourse.controller.LoginController;
import com.techcourse.controller.LoginViewController;
import com.techcourse.controller.LogoutController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;
    private final HandlerKeys handlerKeys;

    public DispatcherServlet() {
        this.handlerKeys = new HandlerKeys();
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        initialize();
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private void initialize() {
        final ControllerContainer container = new ControllerContainer("com.techcourse.controller");
        container.initialize();

        handlerMappings.add(new MapHandlerMapping(handlerKeys,
                Map.of(
                        new HandlerKey("/"), new ForwardController("/index.jsp"),
                        new HandlerKey("/login", RequestMethod.POST), new LoginController(),
                        new HandlerKey("/login", RequestMethod.GET), new LoginViewController(),
                        new HandlerKey("/logout", RequestMethod.POST), new LogoutController()
                )));
        handlerMappings.add(new AnnotationHandlerMapping(handlerKeys, container, "com.techcourse.controller"));
    }

    @Override
    protected void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException {
        final String requestURI = request.getRequestURI();
        log.debug("Method : {}, Request URI : {}", request.getMethod(), requestURI);

        try {
            final var controller = handlerKeys.get(new HandlerKey(request));
            final var result = controller.handle(request, response);
            final View view = convertView(result);
            view.render(Map.of(), request, response);
        } catch (final Exception e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private View convertView(final Object result) {
        if (result.getClass() == String.class) {
            return new JspView((String) result);
        }
        if (result.getClass() == ModelAndView.class) {
            return ((ModelAndView) result).getView();
        }
        throw new IllegalArgumentException(String.format("%s는 지원하지 않는 결과입니다.", result));
    }
}
