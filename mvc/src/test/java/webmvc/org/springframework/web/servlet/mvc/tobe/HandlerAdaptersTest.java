package webmvc.org.springframework.web.servlet.mvc.tobe;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class HandlerAdaptersTest {

	private String handler;

	@BeforeEach
	void setUp() {
		handler = "handler";
	}

	@Test
	void 올바른_HandlerAdapter를_반환한다() {
		// given
		HandlerAdapter trueHandlerAdapter = createHandlerAdapter(true);
		HandlerAdapter falseHandlerAdapter1 = createHandlerAdapter(false);
		HandlerAdapter falseHandlerAdapter2 = createHandlerAdapter(false);
		HandlerAdapters handlerAdapters = new HandlerAdapters(falseHandlerAdapter1, trueHandlerAdapter,
			falseHandlerAdapter2);

		// when
		HandlerAdapter handlerAdapter = handlerAdapters.getHandlerAdapter(handler);

		// then
		assertThat(handlerAdapter).isEqualTo(trueHandlerAdapter);
	}

	private HandlerAdapter createHandlerAdapter(final boolean supports) {
		HandlerAdapter handlerAdapter = Mockito.mock(HandlerAdapter.class);
		given(handlerAdapter.supports(handler))
			.willReturn(supports);
		return handlerAdapter;
	}

	@Test
	void 올바른_HandlerAdapter가_없을때_예외() {
		// given
		String handler = "handler";
		HandlerAdapter falseHandlerAdapter1 = createHandlerAdapter(false);
		HandlerAdapter falseHandlerAdapter2 = createHandlerAdapter(false);
		HandlerAdapters handlerAdapters = new HandlerAdapters(falseHandlerAdapter1, falseHandlerAdapter2);

		// when & then
		assertThatThrownBy(() -> handlerAdapters.getHandlerAdapter(handler))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessage("알맞는 HandlerAdapter가 없습니다.");
	}
}
