package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.web.bind.annotation.ResponseBody;
import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.View;
import com.interface21.webmvc.servlet.view.JsonView;
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

        if (isModelAndView(result)) {
            return (ModelAndView) result;
        }

        if (requiresResponseBody(methodHandler)) {
            return createResponseBodyModelAndView(result);
        }

        if (result instanceof View) {
            return wrapView((View) result);
        }

        throw new IllegalStateException("지원하지 않는 반환 타입입니다: " + result.getClass());
    }

    private boolean isModelAndView(final Object result) {
        return result instanceof ModelAndView;
    }

    private boolean requiresResponseBody(final MethodHandler methodHandler) {
        return hasResponseBody(methodHandler);
    }

    private ModelAndView createResponseBodyModelAndView(final Object result) {
        final Map<String, Object> model = new HashMap<>();
        model.put("data", result);
        return new ModelAndView(new JsonView(), model);
    }

    private ModelAndView wrapView(final View view) {
        return new ModelAndView(view);
    }

    private boolean hasResponseBody(final MethodHandler methodHandler) {
        return methodHandler.getMethod().isAnnotationPresent(ResponseBody.class) ||
                methodHandler.getMethod().getDeclaringClass().isAnnotationPresent(ResponseBody.class);
    }
}
