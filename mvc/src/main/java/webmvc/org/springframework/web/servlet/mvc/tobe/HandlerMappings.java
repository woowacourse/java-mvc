package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.util.Objects;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerMappings {

	private final Set<HandlerMapping> handlerMappings;
	private boolean isInitialized = false;

	public HandlerMappings(final HandlerMapping... handlerMappings) {
		this.handlerMappings = Set.of(handlerMappings);
	}

	public void initialize() {
		if (isInitialized) {
			return;
		}
		handlerMappings.forEach(HandlerMapping::initialize);
		isInitialized = true;
	}

	public Object getHandler(final HttpServletRequest request) {
		return handlerMappings.stream()
			.map(handlerMapping -> handlerMapping.getHandler(request))
			.filter(Objects::nonNull)
			.findAny()
			.orElseThrow(() -> new IllegalArgumentException("요청에 맞는 Hanlder가 없습니다."));
	}
}
