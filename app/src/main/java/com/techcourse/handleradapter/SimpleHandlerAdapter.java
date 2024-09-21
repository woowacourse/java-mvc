package com.techcourse.handleradapter;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof Controller); // Controller 인스턴스인지 확인
    }

    @Override
    public Object handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Controller controller = (Controller) handler;
        return controller.execute(request, response);
    }
}

