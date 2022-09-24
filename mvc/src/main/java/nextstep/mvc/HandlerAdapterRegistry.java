package nextstep.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import nextstep.mvc.handler.adapter.HandlerAdapter;

public class HandlerAdapterRegistry {

	private final List<HandlerAdapter> handlerAdapters;

	public HandlerAdapterRegistry() {
		this.handlerAdapters = new ArrayList<>();
	}

	public void add(HandlerAdapter handlerAdapter) {
		handlerAdapters.add(handlerAdapter);
	}

	public HandlerAdapter getAdapter(final Object handler) {
		return handlerAdapters.stream()
			.filter(adapter -> adapter.supports(handler))
			.findFirst()
			.orElseThrow(() -> new NoSuchElementException("해당 핸들러를 실행시킬 수 있는 어댑터가 없습니다."));
	}
}
