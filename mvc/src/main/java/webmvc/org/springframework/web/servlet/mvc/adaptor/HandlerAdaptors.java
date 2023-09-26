package webmvc.org.springframework.web.servlet.mvc.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import webmvc.org.springframework.web.servlet.view.ModelAndView;

public class HandlerAdaptors {

    private final List<HandlerAdaptor> adaptors;

    public HandlerAdaptors() {
        this.adaptors = new ArrayList<>();
        adaptors.add(new ManualHandlerAdaptor());
        adaptors.add(new AnnotationHandlerAdaptor());
    }

    public ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        final HandlerAdaptor handlerAdaptor = adaptors.stream()
                .filter(adaptor -> adaptor.isHandle(handler))
                .findFirst()
                .orElseThrow();
        return handlerAdaptor.execute(handler, request, response);
    }
}
