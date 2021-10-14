package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import nextstep.mvc.view.JsonViewResolver;
import nextstep.mvc.view.JspViewResolver;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewResolver;

public class HandlerExecution {

    private final Method method;
    private final Object instance;
    private final List<ViewResolver> viewResolvers;

    public HandlerExecution(Method method, Object instance) {
        this.method = method;
        this.instance = instance;
        this.viewResolvers = Arrays.asList(new JsonViewResolver(), new JspViewResolver());
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final Object object = method.invoke(instance, request, response);
        if (object instanceof ModelAndView) {
            return (ModelAndView) object;
        }
        final View view = getView(object);
        return new ModelAndView(view);
    }

    private View getView(Object object) {
        return viewResolvers.stream()
                .filter(viewResolver -> viewResolver.supports(object.getClass()))
                .map(viewResolver -> viewResolver.resolve(object))
                .findFirst()
                .orElseThrow();
    }
}
