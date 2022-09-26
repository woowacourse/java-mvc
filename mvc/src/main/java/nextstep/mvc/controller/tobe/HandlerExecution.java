package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.NullView;
import nextstep.mvc.view.RedirectView;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method handler) {
        this.declaredObject = declaredObject;
        this.method = handler;
    }

    public boolean hasReturnTypeOf(Class<?> returnType) {
        return method.getReturnType().equals(returnType);
    }

    public Object handle2(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return method.invoke(declaredObject, request, response);
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final var returnType = method.getReturnType();
        if (String.class.equals(returnType)) {
            final var viewName = (String) method.invoke(declaredObject, request, response);
            return toModelAndView(viewName);
        }
        if (ModelAndView.class.equals(returnType)) {
            return (ModelAndView) method.invoke(declaredObject, request, response);
        }
        if (Void.TYPE.equals(returnType)) {
            method.invoke(declaredObject, request, response);
            return new ModelAndView(new NullView());
        }
        throw new IllegalArgumentException("처리될 수 없는 로직입니다.");
    }

    private ModelAndView toModelAndView(String viewName) {
        if (viewName.startsWith(RedirectView.REDIRECT_PREFIX)) {
            return new ModelAndView(new RedirectView(viewName));
        }
        return new ModelAndView(new JspView(viewName));
    }
}
