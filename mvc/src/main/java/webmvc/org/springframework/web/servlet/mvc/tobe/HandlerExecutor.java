package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.ModelAndView;

import java.util.HashSet;
import java.util.Set;

public class HandlerExecutor {

    private Set<HandlerAdapter> handlerAdapters = new HashSet<>();

    public ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter.adapt(request, response, handler);
            }
        }
        throw new IllegalArgumentException("Not found handler adapter for handler : " + handler);
    }

    public void addHandlerAdapter(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }
}
