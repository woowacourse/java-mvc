package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.Method;

// 매핑된 컨트롤러 객체의 메서드를 실행하는 역할
public class HandlerExecution {

    private final Object controller;
    private final Method method;

    public HandlerExecution(Object controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(controller, request, response); // 찾아낸 controller의 method 실행하기(invoke)
        // 사용자에게 보여줄 view와 전달할 데이터를 함께 담아서 반환(ModelAndView)
    }
}
