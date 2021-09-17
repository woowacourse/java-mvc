package com.techcourse.air.mvc.core.returnvalue;

import java.lang.reflect.Method;

import com.techcourse.air.mvc.core.view.ModelAndView;
import com.techcourse.air.mvc.web.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;

public class ViewReturnValueHandler implements ReturnValueHandler {

    @Override
    public boolean supportsReturnType(Method method) {
        return !method.isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public ModelAndView handleReturnValue(Object returnValue, HttpServletResponse response) {
        if (returnValue instanceof ModelAndView) {
            return (ModelAndView) returnValue;
        }
        return new ModelAndView(returnValue);
    }
}
