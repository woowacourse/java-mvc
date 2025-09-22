package com.interface21.webmvc.servlet.mvc.adapter;

import com.interface21.webmvc.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    /**
     * Returns 해당 어댑터가 전달받은 핸들러를 지원하는 여부.
     */
    boolean supports(final Object handler);

    /**
     * Returns 지정된 핸들러를 사용하여 주어진 요청을 처리한 ModelAndView.
     */
    ModelAndView handle(final Object handler, final HttpServletRequest request,
        final HttpServletResponse response) throws Exception;
}
