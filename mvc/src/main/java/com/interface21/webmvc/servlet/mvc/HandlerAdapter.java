package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * HandlerMapping 이 찾아준 핸들러(Object)를 실제로 실행하는 어댑터.
 * 핸들러 타입마다 실행 방법이 다르므로, 타입별로 구현체를 하나씩 둔다.
 */
public interface HandlerAdapter {

    /**
     * 이 어댑터가 해당 핸들러를 실행할 수 있는지 판단한다.
     *
     * @param handler HandlerMapping 이 반환한 핸들러
     * @return 처리 가능하면 true
     */
    boolean supports(Object handler);

    /**
     * 핸들러를 실행하고 결과를 ModelAndView 로 통일해서 반환한다.
     * DispatcherServlet 은 핸들러 타입과 무관하게 이 결과만 사용한다.
     */
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
