package com.techcourse.air.mvc.core.returnvalue;

import java.io.IOException;
import java.lang.reflect.Method;

import com.techcourse.air.mvc.core.view.ModelAndView;
import com.techcourse.air.mvc.web.annotation.ResponseBody;

import jakarta.servlet.http.HttpServletResponse;

public class JsonReturnValueHandler extends AbstractMessageConverterHandler {

    @Override
    public boolean supportsReturnType(Method method) {
        return method.isAnnotationPresent(ResponseBody.class);
    }

    @Override
    public ModelAndView handleReturnValue(Object returnValue, HttpServletResponse response) throws IOException {
        writeWithMessageConverter(returnValue, response);
        return null;
    }
}
