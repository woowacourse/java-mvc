package com.interface21.webmvc.servlet.mvc.tobe;

import java.lang.reflect.Method;

import com.interface21.webmvc.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerExecution {

    private final Class clazz;
    private final Method method;

	public HandlerExecution(Class clazz, Method method) {
		this.clazz = clazz;
		this.method = method;
	}

	public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Object object = clazz.getConstructor().newInstance();
        return (ModelAndView) method.invoke(object, request, response);
    }
}
