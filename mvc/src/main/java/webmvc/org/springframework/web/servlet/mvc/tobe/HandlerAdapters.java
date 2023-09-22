package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.Set;

public class HandlerAdapters {

	private final Set<HandlerAdapter> handlerAdapters;

	public HandlerAdapters(final HandlerAdapter... handlerAdapters) {
		this.handlerAdapters = Set.of(handlerAdapters);
	}

	public HandlerAdapter getHandlerAdapter(final Object handler) {
		return handlerAdapters.stream()
			.filter(handlerAdapter -> handlerAdapter.supports(handler))
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("알맞는 HandlerAdapter가 없습니다."));
	}
}
