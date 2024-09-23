package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.AbstractView;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdaptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;

public class ControllerHandlerAdaptor implements HandlerAdaptor {

    private final Controller controller;

    public ControllerHandlerAdaptor(Object handler) {
        if (!isSupport(handler)) {
            throw new RuntimeException("지원하지 않는 핸들러 입니다.");
        }
        this.controller = (Controller) handler;
    }

    @Override
    public boolean isSupport(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String viewName = controller.execute(request, response);
        Class<? extends AbstractView> viewClass = controller.getSupportViewClass();
        Constructor<? extends AbstractView> constructor = viewClass.getConstructor(viewName.getClass());
        AbstractView view = constructor.newInstance(viewName);
        return new ModelAndView(view);
    }
}
