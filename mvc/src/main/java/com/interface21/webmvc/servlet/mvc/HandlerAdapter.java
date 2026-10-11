package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
// 찾은 Handler를 어떻게 실행 시킬지 안다.
public interface HandlerAdapter {

    // 이 Adapter가 해당 Handler를 실행할 수 있는지 판단한다.
    boolean supports(Object handler);

    // Handler를 실행하고 결과를 ModelAndView로 통일한다.
    ModelAndView handle(
            Object handler,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception;
}
