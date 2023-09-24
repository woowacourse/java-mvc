package webmvc.org.springframework.web.servlet.mvc.handlerAdaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import webmvc.org.springframework.web.servlet.mvc.view.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.view.JsonView;
import webmvc.org.springframework.web.servlet.mvc.view.JspView;

public class HandlerAdaptors {

    private final List<Handleradaptor> handleradaptors;

    public HandlerAdaptors() {
        this.handleradaptors = List.of(new ExecutionHandleradaptor());
    }

    public ModelAndView execute(final Object handler, final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        final Handleradaptor findHandleradaptor = findAdaptor(handler);

        final Object result = findHandleradaptor.execute(handler, request, response);

        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        if (result instanceof String) {
            return new ModelAndView(new JspView((String) result));
        }

        final ModelAndView modelAndView = new ModelAndView(new JsonView());
        modelAndView.addObject("model", result);
        return modelAndView;
    }

    private Handleradaptor findAdaptor(final Object handler) {
        return handleradaptors.stream()
                .filter(adaptor -> adaptor.isSame(handler))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
