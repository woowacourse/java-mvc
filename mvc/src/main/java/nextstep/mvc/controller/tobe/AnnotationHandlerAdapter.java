package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerAdapter implements HandlerAdapter {
    private static final Logger log = LoggerFactory.getLogger(HandlerAdapter.class);

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        HandlerExecution handlerExecution = (HandlerExecution) handler;
        try {
            return handlerExecution.handle(request, response);
        } catch (Exception e) {
            log.error("handlerExecution:{}", handlerExecution);
            throw new RuntimeException("handle을 실행하는데 실패했습니다.");
        }
    }
}
