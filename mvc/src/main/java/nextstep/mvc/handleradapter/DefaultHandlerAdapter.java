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
        Arrays.asList(new HandlerExecutionHandlerAdapter(), new ResourceHandlerAdapter()));

    @Override
    public boolean supports(final Object handler) {
        return DEFAULT_ADAPTERS.stream().anyMatch(adapter -> adapter.supports(handler));
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final HandlerAdapter handlerAdapter = findHandlerAdapter(handler);
        try {
            return handlerAdapter.handle(request, response, handler);
        } catch (Exception exception) {
            throw new IllegalArgumentException("핸들러 처리 중에 예외가 발생했습니다.", exception);
        }
    }

    private HandlerAdapter findHandlerAdapter(final Object handler) {
        return DEFAULT_ADAPTERS.stream()
            .filter(adapter -> adapter.supports(handler))
            .findAny()
            .orElseThrow(() -> new IllegalStateException(String.format("지원하지 않는 핸들러입니다.(%s)", handler.getClass())));
    }
}
