package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.View;
import webmvc.org.springframework.web.servlet.view.ViewAdapter;

public class HandlerExecution {

    private final ViewAdapter viewAdapter;
    private final Method method;

    public HandlerExecution(ViewAdapter viewAdapter, Method method) {
        this.viewAdapter = viewAdapter;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        String viewName = (String) method.invoke(getInstance(), request, response);
        View view = viewAdapter.getView(viewName);
        return new ModelAndView(view);
    }

    private Object getInstance() throws ReflectiveOperationException {
        return method.getDeclaringClass()
                .getConstructor()
                .newInstance();
    }
}
