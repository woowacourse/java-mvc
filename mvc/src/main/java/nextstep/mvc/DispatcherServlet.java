package nextstep.mvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.AnnotationHandlerAdaptor;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.ControllerScanner;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JsonViewResolver;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.JspViewResolver;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import nextstep.mvc.view.ViewResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final List<ViewResolver> viewResolvers = new ArrayList<>();

    @Override
    public void init() {
        initHandlerMappings();
        initHandlerAdaptors();
        initViewResolvers();
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            doDispatch(request, response);
        } catch (Throwable e) {
            log.error("Exception : {}", e.getMessage(), e);
            throw new ServletException(e.getMessage());
        }
    }

    private void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecution mappedHandler = getHandler(request);
        if (mappedHandler == null || mappedHandler.getHandler() == null) {
            return;
        }

        HandlerAdapter handlerAdapter = getHandlerAdapter(mappedHandler.getHandler());
        ModelAndView modelAndView = handlerAdapter.handle(request, response, mappedHandler);

        View view = resolveViewName(modelAndView.getViewName(), request);
        view.render(modelAndView, request, response);
    }

    private View resolveViewName(String viewName, HttpServletRequest request) throws Exception {
        for (ViewResolver resolver : viewResolvers) {
            View view = resolver.resolveViewName(viewName, request);
            if (view != null) {
                return view;
            }
        }
        return null;
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        for (HandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.supports(handler)) {
                return handlerAdapter;
            }
        }
        throw new ServletException("[Error] 핸들러 어댑터가 존재하지 않습니다.");
    }

    private HandlerExecution getHandler(HttpServletRequest request) {
        for (HandlerMapping handlerMapping : handlerMappings) {
            HandlerExecution handlerExecution = handlerMapping.getHandler(request);
            if (handlerExecution != null) {
                return handlerExecution;
            }
        }
        return null;
    }

    private void initHandlerMappings() {
        try {
            ControllerScanner controllerScanner = new ControllerScanner("com.techcourse.controller");
            handlerMappings.add(new AnnotationHandlerMapping(controllerScanner));
            for (HandlerMapping handlerMapping : handlerMappings) {
                handlerMapping.initialize();
            }
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private void initHandlerAdaptors() {
        handlerAdapters.add(new AnnotationHandlerAdaptor());
    }

    private void initViewResolvers() {
        viewResolvers.add(new JspViewResolver());
        viewResolvers.add(new JsonViewResolver());
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        try {
            handlerMapping.initialize();
            handlerMappings.add(handlerMapping);

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void addHandlerAdaptor(HandlerAdapter handlerAdapter) {
        handlerAdapters.add(handlerAdapter);
    }

}
