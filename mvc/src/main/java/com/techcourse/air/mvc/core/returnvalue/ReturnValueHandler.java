package com.techcourse.air.mvc.core.returnvalue;

import java.io.IOException;
import java.lang.reflect.Method;

import com.techcourse.air.mvc.core.view.ModelAndView;

import jakarta.servlet.http.HttpServletResponse;

public interface ReturnValueHandler {
    boolean supportsReturnType(Method method);

    ModelAndView handleReturnValue(Object returnValue, HttpServletResponse httpServletResponse) throws IOException;
}
