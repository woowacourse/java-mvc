package com.techcourse;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class HandlerAdapters {

    private static final Logger log = LoggerFactory.getLogger(HandlerAdapters.class);

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(final List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = new ArrayList<>(handlerAdapters);
    }

    public HandlerAdapters() {
        this(Collections.emptyList());
    }

    public void addHandlerAdapter(final HandlerAdapter handlerAdapter) {
        this.handlerAdapters.add(handlerAdapter);
    }

    public void service(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final HandlerAdapter handlerAdapter = handlerAdapters.stream()
                .filter(it -> it.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalStateException("해당 handler를 지원하는 HandlerAdapter를 찾을 수 없습니다!"));

        final ModelAndView modelAndView = handlerAdapter.handle(request, response, handler);

        final boolean isJsonView = modelAndView.getModel().isEmpty();
        if (isJsonView) {
            final Map<String, Object> model = modelAndView.getModel();
            final View view = modelAndView.getView();
            view.render(model, request, response);
            return;
        }
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);
        move(viewName, request, response);
    }

    private void move(final String viewName, final HttpServletRequest request,
                      final HttpServletResponse response) throws Exception {
        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
            return;
        }

        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
        requestDispatcher.forward(request, response);
    }
}
