package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;
import nextstep.mvc.servlet.ViewResolverRegistry;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.viewresolver.JsonViewResolver;
import nextstep.mvc.view.viewresolver.JspViewResolver;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;
    private final ViewResolverRegistry viewResolverRegistry;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
        this.viewResolverRegistry = new ViewResolverRegistry(List.of(
            new JsonViewResolver(),
            new JspViewResolver()
        ));
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
        throws Exception {
        final Object handleResult = method.invoke(declaredObject, request, response);
        if (handleResult instanceof ModelAndView) {
            return (ModelAndView) handleResult;
        }

        final View view = viewResolverRegistry.resolveView(handleResult);
        return new ModelAndView(view);
    }
}
