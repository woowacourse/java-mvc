package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Controller {
    //String으로만 반환하면 화면에 전달할 데이터를 같이 넘길 방법이 부족한 것 같음
    //그래서 Model을 사용한 것이고, Model과 View를 같이 묶은 객체를 사용한듯
    ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res) throws Exception;
}
