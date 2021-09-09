package nextstep.mvc;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.controller.asis.Controller;
import nextstep.mvc.controller.tobe.AnnotationHandlerMapping;
import nextstep.mvc.controller.tobe.HandlerExecution;
import nextstep.mvc.view.JspView;
import nextstep.mvc.view.ModelAndView;
import nextstep.mvc.view.View;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(DispatcherServlet.class);

    private final List<HandlerMapping> handlerMappings;

    public DispatcherServlet() {
        this.handlerMappings = new ArrayList<>();
    }

    @Override
    public void init() {
        addDefaultHandlerMappings();
        handlerMappings.forEach(HandlerMapping::initialize);
    }

    private void addDefaultHandlerMappings() {
        addHandlerMapping(new AnnotationHandlerMapping("com.techcourse"));
    }

    public void addHandlerMapping(HandlerMapping handlerMapping) {
        handlerMappings.add(handlerMapping);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        log.debug("Method : {}, Request URI : {}", request.getMethod(), request.getRequestURI());

        try {
            Object handler = getHandler(request);
            ModelAndView modelAndView = execute(handler, request, response);
            View view = modelAndView.getView();
            view.render(modelAndView.getModel(), request, response);
        } catch (Exception exception) {
            log.error("Exception : {}", exception.getMessage(), exception);
            throw new ServletException(exception.getMessage());
        }
    }

    private Object getHandler(HttpServletRequest request) {
        return handlerMappings.stream()
                .map(handlerMapping -> handlerMapping.getHandler(request))
                .filter(Objects::nonNull)
                .findFirst()
                .orElseThrow();
    }

    private ModelAndView execute(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (handler instanceof HandlerExecution) {
            return ((HandlerExecution) handler).handle(request, response);
        }
        if (handler instanceof Controller) {
            return new ModelAndView(
                    new JspView(
                            ((Controller) handler).execute(request, response)
                    )
            );
        }
        return null;
    }

//    private Controller getController(HttpServletRequest request) {
//        return handlerMappings.stream()
//                .map(handlerMapping -> handlerMapping.getHandler(request))
//                .filter(Objects::nonNull)
//                .map(Controller.class::cast)
//                .findFirst()
//                .orElseThrow();
//    }

//    private void move(String viewName, HttpServletRequest request, HttpServletResponse response) throws Exception {
//        if (viewName.startsWith(JspView.REDIRECT_PREFIX)) {
//            response.sendRedirect(viewName.substring(JspView.REDIRECT_PREFIX.length()));
//            return;
//        }
//
//        final RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewName);
//        requestDispatcher.forward(request, response);
//    }
}
