package com.interface21.webmvc.servlet.mvc;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//애가 그러면 결국 모든 핸들러들을 공통되게 처리하게 해주는 녀석이네
public interface HandlerAdapter {

    // 실행이 가능한 녀석인가 판단해야함
    boolean supports(Object handler);

    // 그리고 실행된 결과도 반환해주면 디스패처 서블릿의 책임이 줄어들듯
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
