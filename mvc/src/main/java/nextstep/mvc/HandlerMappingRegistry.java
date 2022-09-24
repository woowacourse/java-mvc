package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import jakarta.servlet.http.HttpServletRequest;
import nextstep.mvc.handler.mapping.HandlerMapping;

public class HandlerMappingRegistry {

	private final List<HandlerMapping> handlerMappings;

	public HandlerMappingRegistry() {
		this.handlerMappings = new ArrayList<>();
	}

	public void add(HandlerMapping handlerMapping) {
		handlerMappings.add(handlerMapping);
	}

	public void initialize() {
		handlerMappings.forEach(HandlerMapping::initialize);
	}

	public Object getHandler(final HttpServletRequest request) {
		return handlerMappings.stream()
			.map(handlerMapping -> handlerMapping.getHandler(request))
			.filter(Optional::isPresent)
			.map(Optional::get)
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("매핑되는 핸들러가 없습니다."));
	}
}
