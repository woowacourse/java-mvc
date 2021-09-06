package nextstep.mvc.adaptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import nextstep.mvc.view.ModelAndView;

public class HandlerAdapters {

    private final List<HandlerAdapter> handlerAdapters;

    public HandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    public ModelAndView service(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            HandlerAdapter handlerAdapter = findHandlerAdaptor(handler);
            return handlerAdapter.handle(request, response, handler);
        } catch (Exception e) {
            // TODO :: 예외 처리
            throw new IllegalArgumentException("요청 처리 중 예외 발생");
        }
    }

    private HandlerAdapter findHandlerAdaptor(Object handler) {
        return handlerAdapters.stream()
                .filter(handlerAdapter -> handlerAdapter.supports(handler))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("유효한 핸들러 어뎁터를 찾을 수 없습니다."));
    }
}
