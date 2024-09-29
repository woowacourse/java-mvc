package com.techcourse.viewresolver;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.mvc.tobe.ViewResolver;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class SimpleViewResolver implements ViewResolver {

    @Override
    public void resolveView(Object mv, HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView modelAndView = (ModelAndView) mv;
        View view = modelAndView.getView();
        Map<String, Object> model = modelAndView.getModel();
        view.render(model, request, response);
    }
}
