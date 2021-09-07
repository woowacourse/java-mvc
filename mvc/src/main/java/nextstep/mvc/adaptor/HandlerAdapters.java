package nextstep.mvc.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import nextstep.mvc.exception.MvcComponentException;
import nextstep.mvc.view.ModelAndView;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public ModelAndView service(HttpServletRequest request, HttpServletResponse response, Object handler) throws Throwable {
        HandlerAdapter handlerAdapter = findHandlerAdaptor(handler);
        return handlerAdapter.handle(request, response, handler);
    }

    private HandlerAdapter findHandlerAdaptor(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new MvcComponentException("유효한 핸들러 어뎁터를 찾을 수 없습니다."));
    }
}
