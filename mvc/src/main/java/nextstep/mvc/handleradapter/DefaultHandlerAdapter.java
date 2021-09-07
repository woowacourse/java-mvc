package nextstep.mvc.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nextstep.mvc.HandlerAdapter;
import nextstep.mvc.view.ModelAndView;

public class DefaultHandlerAdapter implements HandlerAdapter {

    private static final Set<HandlerAdapter> DEFAULT_ADAPTERS = new HashSet<>(
        Arrays.asList(new ControllerHandlerAdapter(), new HandlerExecutionHandlerAdapter()));

    @Override
    public boolean supports(final Object handler) {
        return DEFAULT_ADAPTERS.stream().anyMatch(adapter -> adapter.supports(handler));
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        return DEFAULT_ADAPTERS.stream()
            .filter(adapter -> adapter.supports(handler))
            .map(adapter -> {
                try {
                    return adapter.handle(request, response, handler);
                } catch (Exception exception) {
                    throw new IllegalArgumentException("핸들러 처리 중에 예외가 발생했습니다.", exception);
                }
            })
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(String.format("처리할 수 없는 핸들러입니다.(%s)", handler.getClass())));
    }
}
