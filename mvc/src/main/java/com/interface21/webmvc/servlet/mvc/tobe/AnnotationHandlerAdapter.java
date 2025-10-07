package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.ResponseBody;
import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof MethodHandler;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        MethodHandler methodHandler = (MethodHandler) handler;
        Object result = methodHandler.handle(request, response);
        return createModelAndView(result, methodHandler);
    }

    private ModelAndView createModelAndView(final Object result, final MethodHandler methodHandler) {
        if (result == null) {
            return null;
        }

        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        if (hasResponseBody(methodHandler)) {
            final Map<String, Object> model = new HashMap<>();
            model.put("data", result);
            return new ModelAndView("json", model);
        }

        if (result instanceof String) {
            return new ModelAndView((String) result);
        }

        return new ModelAndView(result.toString());
    }

    private boolean hasResponseBody(final MethodHandler methodHandler) {
        return methodHandler.getMethod().isAnnotationPresent(ResponseBody.class) ||
                methodHandler.getMethod().getDeclaringClass().isAnnotationPresent(ResponseBody.class);
    }
}
